package com.example.bmimanager.weight.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class WeightRecordDto {
    Long id;
    Long userId;
    Double weight;
    LocalDate recordDate;
    LocalDate createdAt;
}
