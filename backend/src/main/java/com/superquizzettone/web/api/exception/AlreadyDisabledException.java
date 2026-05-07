package com.superquizzettone.web.api.exception;

public class AlreadyDisabledException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public AlreadyDisabledException(String message) {
        super(message);
    }
}
