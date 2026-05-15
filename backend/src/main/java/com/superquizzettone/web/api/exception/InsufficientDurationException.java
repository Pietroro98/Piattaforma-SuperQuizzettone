package com.superquizzettone.web.api.exception;

public class InsufficientDurationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InsufficientDurationException(String message) {
        super(message);
    }
}
