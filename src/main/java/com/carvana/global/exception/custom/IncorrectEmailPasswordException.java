package com.carvana.global.exception.custom;

public class IncorrectEmailPasswordException extends RuntimeException{
    public IncorrectEmailPasswordException(String message) {
        super(message);
    }
}
