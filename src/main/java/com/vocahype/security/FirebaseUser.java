package com.vocahype.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FirebaseUser extends User {
    private String uid;
    private String email;
    private String name;
    private String role;

    public FirebaseUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String name) {
        super(username, password, authorities);
        this.uid = username;
        this.email = email;
        this.name = name;
    }

}

