package com.vocahype.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserLogin extends User {
    private String id;
    public UserLogin(
            final String userId,
            final String email,
            final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = userId;
    }
}
