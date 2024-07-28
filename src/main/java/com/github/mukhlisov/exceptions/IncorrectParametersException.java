package com.github.mukhlisov.exceptions;

public class IncorrectParametersException extends IllegalArgumentException{
    public IncorrectParametersException(String message) {
        super(message);
    }

    public IncorrectParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectParametersException(Throwable cause) {
        super(cause);
    }
}
