package com.github.mukhlisov.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(final String message) {
        super(message);
    }

    public CurrencyNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CurrencyNotFoundException(final Throwable cause) {
        super(cause);
    }

}
