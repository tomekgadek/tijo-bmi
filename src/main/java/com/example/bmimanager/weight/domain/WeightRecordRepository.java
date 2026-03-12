package com.example.bmimanager.weight.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
interface WeightRecordRepository extends Repository<WeightRecord, Long> {

    List<WeightRecord> findByUserIdOrderByRecordDateAsc(Long userId);

    Page<WeightRecord> findByUserIdOrderByRecordDateAsc(Long userId, Pageable pageable);

    Optional<WeightRecord> findTopByUserIdOrderByRecordDateDesc(Long userId);

    WeightRecord save(WeightRecord record);

    Optional<WeightRecord> findById(Long recordId);

    void deleteById(Long recordId);
}
