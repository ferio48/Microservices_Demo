package com.example.microservices.exception;

public final class InvalidObjectDetailsException extends RuntimeException {
    public InvalidObjectDetailsException() {
        super();
    }

    public InvalidObjectDetailsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectDetailsException(String message) {
        super(message);
    }

    public InvalidObjectDetailsException(Throwable cause) {
        super(cause);
    }
}
