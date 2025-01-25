package com.example.microservices2.exception;

public final class S3OperationException extends RuntimeException {
    public S3OperationException() {
        super();
    }

    public S3OperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3OperationException(String message) {
        super(message);
    }

    public S3OperationException(Throwable cause) {
        super(cause);
    }
}
