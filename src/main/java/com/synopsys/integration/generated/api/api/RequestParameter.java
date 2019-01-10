package com.synopsys.integration.generated.api.api;

import org.apache.commons.csv.CSVRecord;

public class RequestParameter {
    private final String parameterKey;
    private final String parameterDescription;

    public static RequestParameter createFromCSVRecord(final CSVRecord csvRecord) {
        final String parameterKey = csvRecord.get(0);
        final String description = csvRecord.get(1);

        return new RequestParameter(parameterKey, description);
    }

    public RequestParameter(final String parameterKey, final String parameterDescription) {
        this.parameterKey = parameterKey;
        this.parameterDescription = parameterDescription;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    public String getParameterDescription() {
        return parameterDescription;
    }
}
