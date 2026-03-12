package com.example.bmimanager.user.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
interface BmiUserRepository extends Repository<BmiUser, Long> {

    Optional<BmiUser> findByUsername(String username);

    List<BmiUser> findByIsPublicTrueAndIsBlockedFalse();

    Optional<BmiUser> findById(Long id);

    BmiUser save(BmiUser user);

    List<BmiUser> findAll();

    void deleteById(Long id);
}
