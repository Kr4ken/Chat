package com.ezhov.exceptions;

public class IncorrectMessageException extends Exception {
    public IncorrectMessageException() {

    }

    public IncorrectMessageException(String message) {
        super(message);
    }
}
