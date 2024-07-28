package com.github.mukhlisov.exceptions;

import java.util.NoSuchElementException;

public class RateNotFoundException extends NoSuchElementException {

    public RateNotFoundException(final String message) {
        super(message);
    }

    public RateNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RateNotFoundException(final Throwable cause) {
        super(cause);
    }
}
