package com.vocahype.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.exception.UserFriendlyException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FailAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Customize the response when authentication fails
        if (authException instanceof InsufficientAuthenticationException) {
            InsufficientAuthenticationException insufficientAuthenticationException =
                    (InsufficientAuthenticationException) authException;
            String detail = insufficientAuthenticationException.getMessage();
            String message = "Authentication failed";

            Map<String, Object> error = new HashMap<>();
            error.put("title", message);
            error.put("detail", detail);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", error);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } else {
            // Handle other authentication exceptions as needed
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}

