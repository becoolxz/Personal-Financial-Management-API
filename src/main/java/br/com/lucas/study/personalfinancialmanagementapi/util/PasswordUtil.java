package br.com.lucas.study.personalfinancialmanagementapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    public static String generateHashBCryptByPassword(String password) {
        if (password == null) {
            return password;
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}

