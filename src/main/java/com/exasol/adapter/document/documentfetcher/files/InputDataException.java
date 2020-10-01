package com.exasol.adapter.document.documentfetcher.files;

/**
 * Exception that is thrown due to errors in the input data.
 */
public class InputDataException extends RuntimeException {
    private static final long serialVersionUID = 4776701726459446831L;

    /**
     * Create a new instance of {@link InputDataException}.
     * 
     * @param message exception message
     * @param cause   causing exception
     */
    public InputDataException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
