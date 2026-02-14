package com.example.bmimanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BmiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private Double height; // w cm

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isBlocked = false;

    @Column(length = 500)
    private String motivationalQuote;

    @Column(length = 500)
    private String achievement;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Builder.Default
    private Integer resultsPerPage = 25;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // TODO: implementacja do innej klasy, BmiUser nie musi miec informacji o paginacji, etc.
    public void setResultsPerPage(Integer resultsPerPage) {
        if (resultsPerPage != null && resultsPerPage > 0 && resultsPerPage <= 100) {
            this.resultsPerPage = resultsPerPage;
        }
    }
}
