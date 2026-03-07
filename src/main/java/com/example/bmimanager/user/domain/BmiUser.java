package com.example.bmimanager.user.domain;

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
class BmiUser {

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
    private Double height;

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

    public void setResultsPerPage(Integer resultsPerPage) {
        if (resultsPerPage != null && resultsPerPage > 0 && resultsPerPage <= 100) {
            this.resultsPerPage = resultsPerPage;
        }
    }

    UserDto dto() {
        return UserDto.builder()
                .id(id)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .height(height)
                .isPublic(isPublic)
                .isBlocked(isBlocked)
                .motivationalQuote(motivationalQuote)
                .achievement(achievement)
                .resultsPerPage(resultsPerPage)
                .build();
    }
}
