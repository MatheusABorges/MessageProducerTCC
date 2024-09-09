package com.MessageProducer.utils;

import java.util.Random;

public class RandomStringGenerator {
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";
    private static final Random random = new Random();

    public static String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        String characters = ALPHANUMERIC_CHARACTERS + SPECIAL_CHARACTERS;

        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return builder.toString();
    }

    public static String generateRandomPredefinedString(int position) {
        String[] possibleStrings = new String[]{"Apple", "Banana", "Cherry", "Date", "Elderberry"};
        return possibleStrings[position];
    }

}