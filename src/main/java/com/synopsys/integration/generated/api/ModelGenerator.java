package com.synopsys.integration.generated.api;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.GsonBuilder;
import com.synopsys.integration.generated.api.api.Endpoint;
import com.synopsys.integration.generated.api.api.FieldSpecification;
import com.synopsys.integration.generated.api.api.RequestParameter;
import com.synopsys.integration.generated.api.api.RequestSpecification;
import com.synopsys.integration.generated.api.api.ResponseLinkSpecification;
import com.synopsys.integration.generated.api.api.ResponseSpecification;
import com.synopsys.integration.rest.HttpMethod;

public class ModelGenerator {
    private static final String API_DIRECTORY_PATH = "endpoints/api";
    private static final String MINIFIED_MEDIA_TYPES_FILE = "minified-media-types.csv";
    private static final String REQUEST_SPEC_PARAMS_FILE = "request-specification-parameters.csv";
    private static final String REQUEST_SPEC_FIELDS_FILE = "request-specification-fields.csv";
    private static final String RESPONSE_SPEC_FIELDS_FILE = "response-specification-fields.csv";
    private static final String RESPONSE_SPEC_LINKS_FILE = "specification-link.csv";

    public static void main(final String[] args) throws Exception {
        final File tempDirectory = Files.createTempDirectory("blackduck-api-generator-temp").toFile();
        System.out.println(String.format("Writing output to: %s", tempDirectory.getCanonicalPath()));

        if (args.length != 1) {
            throw new IllegalArgumentException("Invalid number of arguments. Please prove the path to the unzipped api-specifications folder.");
        }

        final File apiSpecificationsDirectory = new File(args[0]);
        if (!apiSpecificationsDirectory.exists() || !apiSpecificationsDirectory.isDirectory()) {
            throw new IllegalArgumentException("Provided path does not exist or is not a directory.");
        }

        final ModelGenerator modelGenerator = new ModelGenerator(apiSpecificationsDirectory, tempDirectory);
        modelGenerator.generateModel();
    }

    private final File apiSpecificationsDirectory;
    private final File outputDirectory;
    private final MediaTypeManager mediaTypeManager;
    private final FileFinder fileFinder;

    private ModelGenerator(final File apiSpecificationsDirectory, final File outputDirectory) throws IOException {
        this.apiSpecificationsDirectory = apiSpecificationsDirectory;
        this.outputDirectory = outputDirectory;
        final File minifiedMediaTypesCsvFile = new File(apiSpecificationsDirectory, MINIFIED_MEDIA_TYPES_FILE);
        this.mediaTypeManager = MediaTypeManager.fromFile(minifiedMediaTypesCsvFile);
        this.fileFinder = new FileFinder();
    }

    private void generateModel() throws IOException {
        final File apiDirectory = new File(apiSpecificationsDirectory, API_DIRECTORY_PATH);
        final List<Endpoint> endpoints = discoverEndpoints(apiDirectory, fileFinder.extractFinalPieceFromPath(apiDirectory.getPath()));
        final String json = new GsonBuilder().setPrettyPrinting().create().toJson(endpoints);
        System.out.println(json);
    }

    private List<Endpoint> discoverEndpoints(final File currentDirectory, final String currentPath) throws IOException {
        final List<Endpoint> endpoints = new ArrayList<>();
        final List<File> directories = getSubDirectories(currentDirectory);

        for (final File directory : directories) {
            final Optional<HttpMethod> httpMethodOptional = getHttpMethod(directory.getName());

            if (httpMethodOptional.isPresent()) {
                final HttpMethod httpMethod = httpMethodOptional.get();
                final List<Endpoint> discoveredEndpoints = handleHttpMethod(httpMethod, directory, currentPath);
                endpoints.addAll(discoveredEndpoints);
            } else {
                final String newPath = appendToPath(currentPath, directory);
                final List<Endpoint> discoveredEndpoints = discoverEndpoints(directory, newPath);
                endpoints.addAll(discoveredEndpoints);
            }
        }

        return endpoints;
    }

    private final List<File> getSubDirectories(final File directory) {
        final File[] files = directory.listFiles();

        if (files != null) {
            return new ArrayList<>(Arrays.asList(files));
        } else {
            return new ArrayList<>();
        }
    }

    private Optional<HttpMethod> getHttpMethod(final String directoryName) {
        try {
            return Optional.of(HttpMethod.valueOf(directoryName));
        } catch (final IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    private String appendToPath(final String currentPath, final File directory) {
        return currentPath + "/" + fileFinder.extractFinalPieceFromPath(directory.getPath());
    }

    final List<Endpoint> handleHttpMethod(final HttpMethod httpMethod, final File httpMethodDirectory, final String currentPath) throws IOException {
        final List<Endpoint> endpoints = new ArrayList<>();
        final List<File> mediaTypeDirectories = getSubDirectories(httpMethodDirectory);

        for (final File mediaTypeDirectory : mediaTypeDirectories) {
            final String minifiedMediaType = fileFinder.extractFinalPieceFromPath(mediaTypeDirectory.getPath());
            final String mediaType = mediaTypeManager.getMediaType(minifiedMediaType);
            final File requestSpecParamsFile = fileFinder.findFile(mediaTypeDirectory, REQUEST_SPEC_PARAMS_FILE);
            final File requestSpecFieldsFile = fileFinder.findFile(mediaTypeDirectory, REQUEST_SPEC_FIELDS_FILE);
            final RequestSpecification requestSpecification = extractRequestSpecification(requestSpecParamsFile, requestSpecFieldsFile);
            final File responseSpecFieldsFile = fileFinder.findFile(mediaTypeDirectory, RESPONSE_SPEC_FIELDS_FILE);
            final File responseSpecLinksFile = fileFinder.findFile(mediaTypeDirectory, RESPONSE_SPEC_LINKS_FILE);
            final ResponseSpecification responseSpecification = extractResponseSpecification(responseSpecFieldsFile, responseSpecLinksFile);
            final Endpoint endpoint = new Endpoint(currentPath, httpMethod, mediaType, requestSpecification, responseSpecification);

            endpoints.add(endpoint);
        }

        return endpoints;
    }

    private RequestSpecification extractRequestSpecification(final File requestSpecParamsFile, final File requestSpecFieldsFile) throws IOException {
        final RequestSpecification requestSpecification = new RequestSpecification();

        if (requestSpecParamsFile != null && requestSpecParamsFile.exists()) {
            final Reader fileReader = new FileReader(requestSpecParamsFile);
            final CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);

            for (final CSVRecord csvRecord : csvParser) {
                final RequestParameter requestParameter = RequestParameter.createFromCSVRecord(csvRecord);
                requestSpecification.addRequestParameter(requestParameter);
            }
        }

        if (requestSpecFieldsFile != null && requestSpecFieldsFile.exists()) {
            final Reader fileReader = new FileReader(requestSpecFieldsFile);
            final CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);

            for (final CSVRecord csvRecord : csvParser) {
                final FieldSpecification fieldSpecification = FieldSpecification.createFromCSVRecord(csvRecord);
                requestSpecification.addFieldSpecification(fieldSpecification);
            }
        }

        return requestSpecification;
    }

    private ResponseSpecification extractResponseSpecification(final File responseSpecFieldsFile, final File responseSpecLinksFile) throws IOException {
        ResponseSpecification responseSpecification = null;

        if (responseSpecFieldsFile != null && responseSpecFieldsFile.exists()) {
            responseSpecification = new ResponseSpecification();
            final Reader fieldsFileReader = new FileReader(responseSpecFieldsFile);
            final CSVParser fieldsCSVParser = new CSVParser(fieldsFileReader, CSVFormat.DEFAULT);

            for (final CSVRecord csvRecord : fieldsCSVParser) {
                final FieldSpecification fieldSpecification = FieldSpecification.createFromCSVRecord(csvRecord);
                responseSpecification.addFieldSpecification(fieldSpecification);
            }

            if (responseSpecLinksFile != null && responseSpecLinksFile.exists()) {
                final Reader linksFileReader = new FileReader(responseSpecLinksFile);
                final CSVParser linksCSVParser = new CSVParser(linksFileReader, CSVFormat.DEFAULT);

                for (final CSVRecord csvRecord : linksCSVParser) {
                    final ResponseLinkSpecification responseLinkSpecification = ResponseLinkSpecification.createFromCSVRecord(csvRecord);
                    responseSpecification.addLinkSpecification(responseLinkSpecification);
                }
            }
        }

        return responseSpecification;
    }
}
