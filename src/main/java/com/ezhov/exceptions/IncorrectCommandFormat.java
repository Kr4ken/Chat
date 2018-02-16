package com.ezhov.exceptions;

public class IncorrectCommandFormat extends Exception {

    public IncorrectCommandFormat() {
    }

    public IncorrectCommandFormat(String message) {
        super(message);
    }
}
