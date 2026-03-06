package com.example.bmimanager.password;

import org.springframework.stereotype.Service;

@Service
class StrengthValidator {
    public PasswordOutcome validate(String password) {
        if (password == null || password.length() < 8) {
            return PasswordOutcome.TOO_SHORT;
        }
        // At least one digit and one uppercase letter
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasDigit = password.matches(".*\\d.*");

        if (!hasUppercase || !hasDigit) {
            return PasswordOutcome.INVALID;
        }

        return PasswordOutcome.VALID;
    }
}
