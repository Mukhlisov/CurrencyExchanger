package com.github.mukhlisov.exceptions;

public class EmptyCurrencyException extends RuntimeException {
    public EmptyCurrencyException(final String message) {
        super(message);
    }

    public EmptyCurrencyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmptyCurrencyException(final Throwable cause) {
        super(cause);
    }
}
