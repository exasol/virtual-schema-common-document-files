package com.exasol.adapter.document.documentfetcher.files;

/**
 * Exception thrown by the iterator returned by {@link FileLoader} if the source throttled the request.
 */
public class TooManyRequestsException extends RuntimeException {
    private static final long serialVersionUID = -8863070660948424141L;
}
