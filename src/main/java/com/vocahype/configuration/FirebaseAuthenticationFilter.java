package com.vocahype.configuration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.vocahype.entity.Role;
import com.vocahype.entity.User;
import com.vocahype.repository.RoleRepository;
import com.vocahype.repository.UserRepository;
import com.vocahype.security.FirebaseUser;
import com.vocahype.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Map<String, User> userSyncCache = new HashMap<>();

    public UsernamePasswordAuthenticationToken convert(final FirebaseToken decodedToken) {
        String userId = decodedToken.getUid();
        FirebaseUser firebaseUser = new FirebaseUser(decodedToken.getUid(), "", Collections.emptyList(),
                decodedToken.getEmail(), decodedToken.getName());
        if (!userSyncCache.containsKey(userId)) {
            User user = userRepository.findFirstById(userId).orElseGet(() -> {
                Role role = roleRepository.findById(1L).get();
                return userRepository
                        .save(User.builder().id(userId).loginName(firebaseUser.getEmail())
                                .firstName(firebaseUser.getName() != null ? firebaseUser.getEmail() : "User").lastName("")
                                .status(1L).loginCount(0L)
                                .createdOn(Timestamp.valueOf(LocalDateTime.now())).role(role)
                                .build());
            });
            userSyncCache.put(userId, user);
        }
        String role = userSyncCache.get(userId).getRole().getTitle();
        firebaseUser.setRole(role);
        Collection<GrantedAuthority> authorities = GeneralUtils.convert(role);
        return UsernamePasswordAuthenticationToken.authenticated(firebaseUser, "", authorities);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            UsernamePasswordAuthenticationToken authentication = convert(decodedToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (FirebaseAuthException e) {
            if (e.getMessage().startsWith("Firebase ID token has expired")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                GeneralUtils.sendFailAuthenticationBody(response, "Token has expired", e);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void clearCache(final String userId) {
        userSyncCache.remove(userId);
    }
}

