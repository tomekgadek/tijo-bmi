package com.example.bmimanager.bmi.domain;

import java.util.Optional;

final class BmiCalculator {
    private BmiCalculator() {
    }

    public static Optional<Double> calculate(Double weightKg, Double heightCm) {
        if (weightKg == null || heightCm == null || heightCm <= 0) {
            return Optional.empty();
        }
        double heightInMeters = heightCm / 100.0;
        return Optional.of(weightKg / (heightInMeters * heightInMeters));
    }
}
