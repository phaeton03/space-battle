package org.example.exception;

import org.example.exception.base.CustomBaseException;

public class UserNotFoundException extends CustomBaseException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
