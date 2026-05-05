package com.superquizzettone.web.api.exception;

public class UsernameNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public UsernameNotValidException(String message) {
        super(message);
    }
}
