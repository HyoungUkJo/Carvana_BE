package com.carvana.global.exception;

import com.carvana.global.exception.custom.DuplicateEmailException;
import com.carvana.global.exception.custom.FCMException;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import com.carvana.global.exception.custom.ReservationException;
import com.carvana.global.exception.custom.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponseDto("DUPLICATE_EMAIL", e.getMessage()));
    }
    @ExceptionHandler(IncorrectEmailPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleIncorrectEmailPasswordException(IncorrectEmailPasswordException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponseDto("INCORRECT EMAIL OR PASSWORD", e.getMessage()));
    }
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponseDto> ReservationException(ReservationException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponseDto("RESERVATION EXCEPTION", e.getMessage()));
    }

    //FCM Exception Handler
    public ResponseEntity<ErrorResponseDto> handleFcmException(FCMException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponseDto("Can't send Fcm Token", e.getMessage()));
    }
}
