package com.example.bmimanager.weight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "weight_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

    WeightRecordDto dto() {
        return WeightRecordDto.builder()
                .id(id)
                .userId(userId)
                .weight(weight)
                .recordDate(recordDate)
                .createdAt(createdAt)
                .build();
    }

}
