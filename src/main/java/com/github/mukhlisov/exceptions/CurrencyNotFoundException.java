package com.github.mukhlisov.exceptions;

import java.util.NoSuchElementException;

public class CurrencyNotFoundException extends NoSuchElementException {
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
