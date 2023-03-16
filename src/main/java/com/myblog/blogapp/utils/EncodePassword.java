package com.myblog.blogapp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncodePassword {
    public static void main(String[] args) {
        PasswordEncoder encodePassword = new BCryptPasswordEncoder();    //encodePassword obj. will encode our password in 64bit form
        System.out.println(encodePassword.encode("password"));
    }
}
