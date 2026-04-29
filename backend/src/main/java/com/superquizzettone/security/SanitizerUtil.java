package com.superquizzettone.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class SanitizerUtil {

    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return value
                .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                .replace('\b', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String sanitizeNullable(String value) {
        String sanitized = sanitize(value);
        return sanitized == null || sanitized.isBlank() ? null : sanitized;
    }
}
