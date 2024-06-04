package com.vocahype.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

public class BCryptPasswordEncoderCustom extends BCryptPasswordEncoder {
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    public BCryptPasswordEncoderCustom() {
        super();
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        if (encodedPassword != null && encodedPassword.length() != 0) {
            if (BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            } else {
                return rawPassword.toString().equals(encodedPassword);
            }
        }
        return false;
    }
}
