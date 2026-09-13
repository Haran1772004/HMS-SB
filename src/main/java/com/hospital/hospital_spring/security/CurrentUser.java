package com.hospital.hospital_spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    private CurrentUser() {
    }

    public static Authentication getAuthentication() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    public static String getUsername() {

        Authentication authentication =
                getAuthentication();

        if (authentication == null) {
            return null;
        }

        return authentication.getName();
    }

    public static int getUserId() {

        Authentication authentication =
                getAuthentication();

        if (authentication == null) {
            return 0;
        }

        Object details =
                authentication.getDetails();

        if (details instanceof Integer) {
            return (Integer) details;
        }

        return 0;
    }

    public static String getRole() {

        Authentication authentication =
                getAuthentication();

        if (authentication == null) {
            return null;
        }

        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority ->
                        authority.getAuthority()
                )
                .orElse(null);
    }
}