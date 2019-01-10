package com.synopsys.integration.generated.api.api;

import java.util.ArrayList;
import java.util.List;

public class ResponseSpecification {
    private final List<FieldSpecification> responseSpecifications = new ArrayList<>();

    public void addFieldSpecification(final FieldSpecification fieldSpecification) {
        if (fieldSpecification != null) {
            responseSpecifications.add(fieldSpecification);
        }
    }

    public List<FieldSpecification> getResponseSpecifications() {
        return responseSpecifications;
    }
}
