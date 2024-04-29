package com.project.authappbackend.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {
    public static String generateRandomPassword() {
        // Generate a random alphanumeric string
        return RandomStringUtils.randomAlphanumeric(6);
    }

    public static String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static void deliverPasswordViaMockService(String email, String password) {
        // Simulate delivery of password via email/sms (log to terminal)
        System.out.println("Password sent to " + email + ": " + password);
    }

    public static boolean passwordsMatch(String oldPassword, String newPassword) {
        return oldPassword.equals(newPassword);
    }
}
