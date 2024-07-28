package com.github.mukhlisov.exceptions;

import java.sql.SQLException;

public class EntityInsertException extends SQLException {
    public EntityInsertException(String message) {
        super(message);
    }

    public EntityInsertException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityInsertException(Throwable cause) {
        super(cause);
    }
}
