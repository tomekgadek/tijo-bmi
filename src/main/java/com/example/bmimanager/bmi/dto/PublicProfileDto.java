package com.example.bmimanager.bmi.dto;

import com.example.bmimanager.bmi.domain.BmiUser;

public record PublicProfileDto(BmiUser user, Double currentWeight, Double currentBMI) {
}
