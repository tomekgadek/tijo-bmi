package com.example.bmimanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BmiUser bmiUser;

    @Column(nullable = false)
    private Double weight; // w kg

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

    // TODO: do poprawy, logika biznesowa do odzielnej klasy
    public Double calculateBMI() {
        if (bmiUser != null && bmiUser.getHeight() != null && weight != null) {
            double heightInMeters = bmiUser.getHeight() / 100.0;
            return weight / (heightInMeters * heightInMeters);
        }
        return null;
    }
}
