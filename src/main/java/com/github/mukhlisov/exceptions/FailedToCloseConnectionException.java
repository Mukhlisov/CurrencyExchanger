package com.github.mukhlisov.exceptions;

public class FailedToCloseConnectionException extends Exception{

    public FailedToCloseConnectionException(final String message){
        super(message);
    }

    public FailedToCloseConnectionException(final String message, final Throwable cause){
        super(message, cause);
    }

    public FailedToCloseConnectionException(final Throwable cause){
        super(cause);
    }
}
