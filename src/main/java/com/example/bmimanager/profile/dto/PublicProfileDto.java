package com.example.bmimanager.profile.dto;

import com.example.bmimanager.user.domain.BmiUser;

public record PublicProfileDto(BmiUser user, Double currentWeight, Double currentBMI) {
}
