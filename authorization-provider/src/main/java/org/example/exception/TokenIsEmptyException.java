package org.example.exception;

import org.example.exception.base.CustomBaseException;

public class TokenIsEmptyException extends CustomBaseException {
    public TokenIsEmptyException() {
        super();
    }

    public TokenIsEmptyException(String message) {
        super(message);
    }
}
