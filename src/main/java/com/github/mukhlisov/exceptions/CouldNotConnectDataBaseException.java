package com.github.mukhlisov.exceptions;

public class CouldNotConnectDataBaseException extends RuntimeException {
    public CouldNotConnectDataBaseException(Throwable cause) {
        super(cause);
    }

    public CouldNotConnectDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotConnectDataBaseException(final String message) {
        super(message);
    }
}
