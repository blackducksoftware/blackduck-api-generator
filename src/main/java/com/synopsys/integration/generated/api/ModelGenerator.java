package com.synopsys.integration.generated.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.synopsys.integration.generated.api.extraction.Endpoint;
import com.synopsys.integration.generated.api.extraction.FileFinder;
import com.synopsys.integration.generated.api.extraction.MediaTypeManager;
import com.synopsys.integration.generated.api.extraction.SpecificationExtractor;

public class ModelGenerator {
    public static final String API_DIRECTORY_PATH = "endpoints/api";
    public static final String MINIFIED_MEDIA_TYPES_FILE = "minified-media-types.csv";
    public static final String REQUEST_SPEC_PARAMS_FILE = "request-specification-parameters.csv";
    public static final String REQUEST_SPEC_FIELDS_FILE = "request-specification-fields.csv";
    public static final String RESPONSE_SPEC_FIELDS_FILE = "response-specification-fields.csv";
    public static final String RESPONSE_SPEC_LINKS_FILE = "specification-link.csv";

    public static final String DIRECTORY_PREFIX = "/com/synopsys/integration/blackduck/api/generated";
    public static final String DISCOVERY_DIRECTORY = DIRECTORY_PREFIX + "/discovery";
    public static final String ENUM_DIRECTORY = DIRECTORY_PREFIX + "/enumeration";
    public static final String VIEW_DIRECTORY = DIRECTORY_PREFIX + "/view";
    public static final String RESPONSE_DIRECTORY = DIRECTORY_PREFIX + "/response";
    public static final String COMPONENT_DIRECTORY = DIRECTORY_PREFIX + "/component";

    public static final String API_CORE_PACKAGE_PREFIX = "com.synopsys.integration.blackduck.api.core";
    public static final String GENERATED_PACKAGE_PREFIX = "com.synopsys.integration.blackduck.api.generated";
    public static final String DISCOVERY_PACKAGE = GENERATED_PACKAGE_PREFIX + ".discovery";
    public static final String ENUM_PACKAGE = GENERATED_PACKAGE_PREFIX + ".enumeration";
    public static final String VIEW_PACKAGE_SUFFIX = ".view";
    public static final String RESPONSE_PACKAGE_SUFFIX = ".response";
    public static final String COMPONENT_PACKAGE_SUFFIX = ".component";
    public static final String VIEW_PACKAGE = GENERATED_PACKAGE_PREFIX + VIEW_PACKAGE_SUFFIX;
    public static final String RESPONSE_PACKAGE = GENERATED_PACKAGE_PREFIX + RESPONSE_PACKAGE_SUFFIX;
    public static final String COMPONENT_PACKAGE = GENERATED_PACKAGE_PREFIX + COMPONENT_PACKAGE_SUFFIX;

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

    private ModelGenerator(final File apiSpecificationsDirectory, final File outputDirectory) {
        this.apiSpecificationsDirectory = apiSpecificationsDirectory;
        this.outputDirectory = outputDirectory;

    }

    private void generateModel() throws IOException {
        final File minifiedMediaTypesCsvFile = new File(apiSpecificationsDirectory, MINIFIED_MEDIA_TYPES_FILE);
        final MediaTypeManager mediaTypeManager = MediaTypeManager.fromFile(minifiedMediaTypesCsvFile);
        final FileFinder fileFinder = new FileFinder();
        final SpecificationExtractor specificationExtractor = new SpecificationExtractor(mediaTypeManager, fileFinder);

        final File apiDirectory = new File(apiSpecificationsDirectory, API_DIRECTORY_PATH);
        final List<Endpoint> endpoints = specificationExtractor.discoverEndpoints(apiDirectory, fileFinder.extractFinalPieceFromPath(apiDirectory.getPath()));
        final String json = new GsonBuilder().setPrettyPrinting().create().toJson(endpoints);
        System.out.println(json);
    }

    private void create(final List<Endpoint> endpoints) {

    }
}
