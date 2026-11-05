package com.epiis.apirfds220261.helper;

import java.security.SecureRandom;

public class GenericHelper {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARACTERS =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private GenericHelper() {
    }

    public static String followCodeGeneration() {
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}