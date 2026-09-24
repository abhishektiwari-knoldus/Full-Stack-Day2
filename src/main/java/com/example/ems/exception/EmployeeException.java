package com.example.ems.exception;

/** Base class for all application-specific errors, so callers can catch them in one place. */
public class EmployeeException extends RuntimeException {
    public EmployeeException(String message) {
        super(message);
    }
}
