package com.aivle.bit.global.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordUtil {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 20;

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();

        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_+=<>?";

        StringBuilder password = new StringBuilder();
        List<Character> passwordCharacters = new ArrayList<>();

        passwordCharacters.add(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        passwordCharacters.add(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        passwordCharacters.add(numbers.charAt(random.nextInt(numbers.length())));
        passwordCharacters.add(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        int remainingLength = PASSWORD_MIN_LENGTH + random.nextInt(PASSWORD_MAX_LENGTH - PASSWORD_MIN_LENGTH + 1) - 4;

        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

        for (int i = 0; i < remainingLength; i++) {
            passwordCharacters.add(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        Collections.shuffle(passwordCharacters);

        for (char c : passwordCharacters) {
            password.append(c);
        }

        return password.toString();
    }
}
