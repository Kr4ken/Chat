package com.ezhov.exceptions;

// TODO: Сделать более осмысленную ошибку
public class IncorrectMessageException extends Exception {

   public IncorrectMessageException(String message){
        super(message);
    }
}
