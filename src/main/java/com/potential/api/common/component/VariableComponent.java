package com.potential.api.common.component;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VariableComponent {
    @Value("${CHAR_LOWER}")
    private static String CHAR_LOWER;

    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();

    @Value("${NUMBER}")
    private static String NUMBER;

    @Value("${SPECIAL_CHARS}")
    private static String SPECIAL_CHARS;

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHARS;
    private static final SecureRandom random = new SecureRandom();

    public String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            int rndCharAt = random.nextInt(PASSWORD_ALLOW_BASE.length());
            char rndChar = PASSWORD_ALLOW_BASE.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }
}
