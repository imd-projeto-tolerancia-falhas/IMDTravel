package com.imdtravel.exceptions;

public class CircuitBreakerIsOpenException extends RuntimeException {
    public CircuitBreakerIsOpenException(String message) {
        super(message);
    }
}
