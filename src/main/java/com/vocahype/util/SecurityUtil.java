package com.vocahype.util;

import com.vocahype.exception.InvalidException;
import com.vocahype.security.FirebaseUser;
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
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof FirebaseUser) {
            return ((FirebaseUser) authentication.getPrincipal()).getEmail();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof FirebaseUser) {
            return ((FirebaseUser) authentication.getPrincipal()).getUid();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }

    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof FirebaseUser) {
            return ((FirebaseUser) authentication.getPrincipal()).getName();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }

    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof FirebaseUser) {
            return ((FirebaseUser) authentication.getPrincipal()).getRole();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }
}

