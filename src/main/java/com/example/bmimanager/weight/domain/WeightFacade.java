package com.example.bmimanager.weight.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class WeightFacade {

    private final WeightRecordRepository weightRecordRepository;

    public WeightFacade(WeightRecordRepository weightRecordRepository) {
        this.weightRecordRepository = weightRecordRepository;
    }

    public WeightRecordDto addWeightRecord(Long userId, Double weight) {
        WeightRecord record = WeightRecord.builder()
                .userId(userId)
                .weight(weight)
                .recordDate(LocalDate.now())
                .build();
        return weightRecordRepository.save(record).dto();
    }

    public WeightRecordDto addWeightRecord(Long userId, Double weight, LocalDate recordDate) {
        WeightRecord record = WeightRecord.builder()
                .userId(userId)
                .weight(weight)
                .recordDate(recordDate)
                .build();
        return weightRecordRepository.save(record).dto();
    }

    public List<WeightRecordDto> getUserWeightRecords(Long userId) {
        return weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId).stream().map(WeightRecord::dto).toList();
    }

    public Page<WeightRecordDto> getPaginatedUserWeightRecords(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId, pageable).map(WeightRecord::dto);
    }

    public void deleteWeightRecord(Long recordId) {
        weightRecordRepository.deleteById(recordId);
    }

    public Optional<WeightRecordDto> getWeightRecordById(Long recordId) {
        return weightRecordRepository.findById(recordId).map(WeightRecord::dto);
    }

    public Optional<Double> getCurrentWeight(Long userId) {
        return weightRecordRepository.findTopByUserIdOrderByRecordDateDesc(userId)
                .map(WeightRecord::getWeight);
    }

    public Optional<WeightRecordDto> updateWeightRecord(Long recordId, Double weight, LocalDate recordDate) {
        return weightRecordRepository.findById(recordId).map(record -> {
            record.setWeight(weight);
            record.setRecordDate(recordDate);
            return weightRecordRepository.save(record).dto();
        });
    }

    public Optional<Double> getLowestWeight(Long userId) {
        List<WeightRecord> records = weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId);
        return records.stream()
                .map(WeightRecord::getWeight)
                .min(Double::compare);
    }

    public Optional<Double> getHighestWeight(Long userId) {
        List<WeightRecord> records = weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId);
        return records.stream()
                .map(WeightRecord::getWeight)
                .max(Double::compare);
    }
}
