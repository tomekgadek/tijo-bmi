package com.example.bmimanager.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BmiUserRepository extends JpaRepository<BmiUser, Long> {
    Optional<BmiUser> findByUsername(String username);

    List<BmiUser> findByIsPublicTrueAndIsBlockedFalse();
}
