package com.example.bmimanager.bmi.domain;

final class BmiCalculator {
    private BmiCalculator() {
    }

    public static Double calculate(Double weightKg, Double heightCm) {
        if (weightKg == null || heightCm == null || heightCm <= 0) {
            return null;
        }
        double heightInMeters = heightCm / 100.0;
        return weightKg / (heightInMeters * heightInMeters);
    }
}
