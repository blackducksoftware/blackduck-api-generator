package com.synopsys.integration.generated.api.api;

import org.apache.commons.csv.CSVRecord;

public class ResponseLinkSpecification {
    private final String key;
    private final boolean optional;
    private final String description;

    public static ResponseLinkSpecification createFromCSVRecord(final CSVRecord csvRecord) {
        final String key = csvRecord.get(0);
        final boolean optional = Boolean.parseBoolean(csvRecord.get(1));
        final String description = csvRecord.get(2);

        return new ResponseLinkSpecification(key, optional, description);
    }

    public ResponseLinkSpecification(final String key, final boolean optional, final String description) {
        this.key = key;
        this.optional = optional;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getDescription() {
        return description;
    }
}
