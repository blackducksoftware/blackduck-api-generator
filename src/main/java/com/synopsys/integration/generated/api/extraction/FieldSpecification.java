package com.synopsys.integration.generated.api.extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public class FieldSpecification {
    final String propertyPath;
    final String type;
    final boolean optional;
    final String description;
    final List<String> allowedValues;

    public static FieldSpecification createFromCSVRecord(final CSVRecord csvRecord) {
        final String propertyPath = csvRecord.get(0);
        final String type = csvRecord.get(1);
        final boolean optional = Boolean.parseBoolean(csvRecord.get(2));
        final String description = csvRecord.get(3);
        final List<String> allowedValues = Arrays.asList(csvRecord.get(4).split(","));

        return new FieldSpecification(propertyPath, type, optional, description, allowedValues);
    }

    public FieldSpecification(final String propertyPath, final String type, final boolean optional, final String description, final List<String> allowedValues) {
        this.propertyPath = propertyPath;
        this.type = type;
        this.optional = optional;
        this.description = description;
        this.allowedValues = allowedValues;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAllowedValues() {
        if (allowedValues == null) {
            return new ArrayList<>();
        } else {
            return allowedValues;
        }
    }
}
