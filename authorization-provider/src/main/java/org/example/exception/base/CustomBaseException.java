package org.example.exception.base;

public class CustomBaseException extends RuntimeException {
    public CustomBaseException() {
        super();
    }

    public CustomBaseException(String message) {
        super(message);
    }
}
