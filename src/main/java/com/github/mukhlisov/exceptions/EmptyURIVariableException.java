package com.github.mukhlisov.exceptions;

public class EmptyURIVariableException extends RuntimeException {
    public EmptyURIVariableException(final String message) {
        super(message);
    }

    public EmptyURIVariableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmptyURIVariableException(final Throwable cause) {
        super(cause);
    }
}
