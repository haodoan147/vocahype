package com.vocahype.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        Timestamp exp = Timestamp.from(source.getClaim("exp"));
        String userId = source.getClaim("user_id");
        String email = source.getClaim("email");
        if (exp == null || exp.before(new Timestamp(System.currentTimeMillis()))) {
            UserLogin user = new UserLogin(userId, email, "", Collections.emptySet());
            return UsernamePasswordAuthenticationToken.authenticated(user, "", Collections.emptySet());
        }
        UserLogin user = new UserLogin(userId, email, "",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        return UsernamePasswordAuthenticationToken.authenticated(user, "", Collections.emptySet());
    }

}
