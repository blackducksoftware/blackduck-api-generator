package com.synopsys.integration.generated.api.extraction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.Slf4jIntLogger;

public class MediaTypeManager {
    private static final IntLogger logger = new Slf4jIntLogger(LoggerFactory.getLogger(MediaTypeManager.class));

    private final Map<String, String> minifiedMediaTypeMap;

    public static MediaTypeManager fromFile(final File minifiedMediaTypeFile) throws IOException {
        final Reader csvParserReader = new FileReader(minifiedMediaTypeFile);
        final CSVParser mediaTypeCSVParser = new CSVParser(csvParserReader, CSVFormat.DEFAULT);

        return MediaTypeManager.fromCSVParser(mediaTypeCSVParser);
    }

    public static MediaTypeManager fromCSVParser(final CSVParser csvParser) {
        final Map<String, String> minifiedMediaTypeMap = new HashMap<>();
        for (final CSVRecord csvRecord : csvParser) {
            if (csvRecord.size() == 2) {
                minifiedMediaTypeMap.put(csvRecord.get(1), csvRecord.get(0));
            } else {
                logger.warn(String.format("Record %d does not have the expected number of columns", csvRecord.getRecordNumber()));
            }
        }

        return new MediaTypeManager(minifiedMediaTypeMap);
    }

    public MediaTypeManager(final Map<String, String> minifiedMediaTypeMap) {
        this.minifiedMediaTypeMap = minifiedMediaTypeMap;
    }

    public String getMediaType(final String minifiedMediaType) {
        return minifiedMediaTypeMap.get(minifiedMediaType);
    }
}
