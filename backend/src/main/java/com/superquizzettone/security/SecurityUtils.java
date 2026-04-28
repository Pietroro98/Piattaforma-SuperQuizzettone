package com.superquizzettone.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> role.equals(auth.getAuthority()));
    }

    public static boolean isAdministrator() {
        return hasRole("ROLE_ADMINISTRATOR");
    }

    public static boolean isPlayer() {
        return hasRole("ROLE_PLAYER");
    }

    public static boolean isWriter() {
        return hasRole("ROLE_WRITER");
    }

    public static boolean isReviewer() {
        return hasRole("ROLE_REVIEWER");
    }

    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
