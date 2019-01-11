package com.synopsys.integration.generated.api.extraction;

import java.util.Optional;

import com.synopsys.integration.rest.HttpMethod;

public class Endpoint {
    private final String path;
    private final HttpMethod httpMethod;
    private final String mediaType;
    private final RequestSpecification requestSpecification;
    private final ResponseSpecification responseSpecification;

    public Endpoint(final String path, final HttpMethod httpMethod, final String mediaType, final RequestSpecification requestSpecification, final ResponseSpecification responseSpecification) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
        this.requestSpecification = requestSpecification;
        this.responseSpecification = responseSpecification;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Optional<RequestSpecification> getRequestSpecification() {
        return Optional.ofNullable(requestSpecification);
    }

    public Optional<ResponseSpecification> getResponseSpecification() {
        return Optional.ofNullable(responseSpecification);
    }
}
