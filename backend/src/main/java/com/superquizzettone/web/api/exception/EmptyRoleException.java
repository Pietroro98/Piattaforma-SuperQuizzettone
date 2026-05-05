package com.superquizzettone.web.api.exception;

import org.springframework.security.access.AccessDeniedException;

public class EmptyRoleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyRoleException(String username) {
        super("Insufficient authorities for user '" + username + "' to access this resource");
    }
}
