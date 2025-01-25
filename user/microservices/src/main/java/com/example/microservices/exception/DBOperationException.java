package com.example.microservices.exception;

public final class DBOperationException extends RuntimeException{
    public DBOperationException() {
        super();
    }

    public DBOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBOperationException(String message) {
        super(message);
    }

    public DBOperationException(Throwable cause) {
        super(cause);
    }
}
