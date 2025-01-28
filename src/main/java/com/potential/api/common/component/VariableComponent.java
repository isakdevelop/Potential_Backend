package com.potential.api.common.component;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VariableComponent {
    @Value("${NUMBER}")
    private String number;

    @Value("${PASSWORD_LENGTH}")
    private int passwordLength;

    private final SecureRandom random = new SecureRandom();

    public String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int rndCharAt = random.nextInt(number.length());
            char rndChar = number.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }
}
