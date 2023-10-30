package com.vocahype.util;

import com.vocahype.exception.InvalidException;
import com.vocahype.security.UserLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != null) {
            return authentication.getPrincipal().toString();
        }
        throw new InvalidException("Authentication Fail", "Current Authentication userId is null");
    }

}

