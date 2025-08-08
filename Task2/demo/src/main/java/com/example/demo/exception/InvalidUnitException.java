package com.example.demo.exception;

public class InvalidUnitException extends RuntimeException {
    public InvalidUnitException(String message) {
        super(message);
    }
}