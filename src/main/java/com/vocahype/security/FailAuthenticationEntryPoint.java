package com.vocahype.security;

import com.vocahype.util.GeneralUtils;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FailAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // Customize the response when authentication fails
        if (authException instanceof InsufficientAuthenticationException) {
            InsufficientAuthenticationException insufficientAuthenticationException =
                    (InsufficientAuthenticationException) authException;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            GeneralUtils.sendFailAuthenticationBody(response, "Authentication failed", insufficientAuthenticationException);
        } else {
            // Handle other authentication exceptions as needed
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}

