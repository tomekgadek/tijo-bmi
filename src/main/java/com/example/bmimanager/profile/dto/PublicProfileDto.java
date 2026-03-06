package com.example.bmimanager.profile.dto;

import com.example.bmimanager.user.domain.UserDto;

public record PublicProfileDto(UserDto user, Double currentWeight, Double currentBMI) {
}
