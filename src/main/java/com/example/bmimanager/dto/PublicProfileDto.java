package com.example.bmimanager.dto;

import com.example.bmimanager.entity.BmiUser;

public record PublicProfileDto (BmiUser user, Double currentWeight, Double currentBMI) { }
