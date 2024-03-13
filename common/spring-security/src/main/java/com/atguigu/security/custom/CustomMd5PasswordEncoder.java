package com.atguigu.security.custom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import utils.MD5;
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {

    public String encode( CharSequence rawPassword ) {
        return MD5.encrypt(rawPassword.toString());
    }

    public boolean matches( CharSequence rawPassword, String encodedPassword ) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}
