package com.superquizzettone.web.api.exception;
import org.springframework.security.access.AccessDeniedException;

public class ForbiddenException extends AccessDeniedException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(String username) {
        super("Insufficient authorities for user '" + username + "' to access this resource");
    }
}
