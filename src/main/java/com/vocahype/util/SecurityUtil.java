package com.vocahype.util;

import com.google.firebase.auth.FirebaseToken;
import com.vocahype.exception.InvalidException;
import com.vocahype.security.FirebaseAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null && authentication instanceof FirebaseAuthentication) {
            return ((FirebaseToken) authentication.getDetails()).getEmail();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null && authentication instanceof FirebaseAuthentication) {
            return ((FirebaseAuthentication) authentication).getUid();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }

    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null && authentication instanceof FirebaseAuthentication) {
            return ((FirebaseToken) authentication.getDetails()).getName();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }
}

