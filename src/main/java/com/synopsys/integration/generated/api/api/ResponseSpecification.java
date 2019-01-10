package com.synopsys.integration.generated.api.api;

import java.util.ArrayList;
import java.util.List;

public class ResponseSpecification {
    private final List<FieldSpecification> fieldSpecifications = new ArrayList<>();
    private final List<ResponseLinkSpecification> linkSpecifications = new ArrayList<>();

    public void addFieldSpecification(final FieldSpecification fieldSpecification) {
        if (fieldSpecification != null) {
            fieldSpecifications.add(fieldSpecification);
        }
    }

    public void addLinkSpecification(final ResponseLinkSpecification responseLinkSpecification) {
        if (responseLinkSpecification != null) {
            linkSpecifications.add(responseLinkSpecification);
        }
    }

    public List<FieldSpecification> getFieldSpecifications() {
        return fieldSpecifications;
    }

    public List<ResponseLinkSpecification> getLinkSpecifications() {
        return linkSpecifications;
    }
}
