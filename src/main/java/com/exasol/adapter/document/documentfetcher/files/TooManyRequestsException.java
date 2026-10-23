package com.exasol.adapter.document.documentfetcher.files;

/**
 * Exception thrown by the iterator returned by {@link RemoteFileFinder} if the source throttled the request.
 */
public class TooManyRequestsException extends RuntimeException {
    private static final long serialVersionUID = 1485753394282586784L;

    /**
     * Creates an exception for a throttled request.
     */
    public TooManyRequestsException() {
        // Default constructor.
    }
}
