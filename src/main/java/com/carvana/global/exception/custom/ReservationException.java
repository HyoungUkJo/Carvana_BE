package com.carvana.global.exception.custom;

public class ReservationException extends RuntimeException{
    public ReservationException(String message) {
        super(message);
    }
}
