package com.example.bmimanager.repository;

import com.example.bmimanager.entity.User;
import com.example.bmimanager.entity.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    List<WeightRecord> findByUserOrderByRecordDateAsc(User user);

    @Query("SELECT wr FROM WeightRecord wr WHERE wr.user = :user AND wr.recordDate BETWEEN :startDate AND :endDate ORDER BY wr.recordDate ASC")
    List<WeightRecord> findByUserAndDateRange(@Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<WeightRecord> findByUser(User user);

    org.springframework.data.domain.Page<WeightRecord> findByUserOrderByRecordDateDesc(User user,
            org.springframework.data.domain.Pageable pageable);
}
