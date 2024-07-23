package com.github.mukhlisov.exceptions;

public class DataBaseOperationException extends Exception{

    public DataBaseOperationException(final String message){
        super(message);
    }

    public DataBaseOperationException(final String message, final Throwable cause){
        super(message, cause);
    }

    public DataBaseOperationException(final Throwable cause){
        super(cause);
    }
}
