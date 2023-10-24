package com.vocahype.security;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class FirebaseAuthentication implements Authentication {
    private final FirebaseToken firebaseToken;
    private boolean authenticated = true;

    public FirebaseAuthentication(FirebaseToken firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // You can implement this based on your requirements.
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return firebaseToken;
    }

    @Override
    public Object getPrincipal() {
        return firebaseToken.getUid();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return firebaseToken.getName();
    }

    public String getUid() {
        return firebaseToken.getUid();
    }
}

