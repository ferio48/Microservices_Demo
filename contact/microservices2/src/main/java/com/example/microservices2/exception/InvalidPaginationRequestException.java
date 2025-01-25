package com.example.microservices2.exception;

public final class InvalidPaginationRequestException extends RuntimeException{
    public InvalidPaginationRequestException() {
        super();
    }

    public InvalidPaginationRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPaginationRequestException(String message) {
        super(message);
    }

    public InvalidPaginationRequestException(Throwable cause) {
        super(cause);
    }
}
