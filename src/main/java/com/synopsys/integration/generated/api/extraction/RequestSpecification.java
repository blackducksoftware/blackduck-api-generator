package com.synopsys.integration.generated.api.extraction;

import java.util.ArrayList;
import java.util.List;

public class RequestSpecification {
    private final List<RequestParameter> requestParameters = new ArrayList<>();
    private final List<FieldSpecification> fieldSpecifications = new ArrayList<>();

    public void addRequestParameter(final RequestParameter requestParameter) {
        requestParameters.add(requestParameter);
    }

    public void addFieldSpecification(final FieldSpecification fieldSpecification) {
        if (fieldSpecification != null) {
            fieldSpecifications.add(fieldSpecification);
        }
    }

    public List<RequestParameter> getRequestParameters() {
        return requestParameters;
    }

    public List<FieldSpecification> getFieldSpecifications() {
        return fieldSpecifications;
    }

}
