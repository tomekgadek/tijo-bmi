package com.example.bmimanager.weight.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

    List<WeightRecord> findByUserIdOrderByRecordDateAsc(Long userId);

    Page<WeightRecord> findByUserIdOrderByRecordDateAsc(Long userId, Pageable pageable);

    Optional<WeightRecord> findTopByUserIdOrderByRecordDateDesc(Long userId);

}
