package com.example.bmimanager.weight.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

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
        return mapToDto(weightRecordRepository.save(record));
    }

    public WeightRecordDto addWeightRecord(Long userId, Double weight, LocalDate recordDate) {
        WeightRecord record = WeightRecord.builder()
                .userId(userId)
                .weight(weight)
                .recordDate(recordDate)
                .build();
        return mapToDto(weightRecordRepository.save(record));
    }

    public List<WeightRecordDto> getUserWeightRecords(Long userId) {
        return weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId).stream().map(this::mapToDto).toList();
    }

    public Page<WeightRecordDto> getPaginatedUserWeightRecords(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId, pageable).map(this::mapToDto);
    }

    public void deleteWeightRecord(Long recordId) {
        weightRecordRepository.deleteById(recordId);
    }

    public WeightRecordDto getWeightRecordById(Long recordId) {
        return weightRecordRepository.findById(recordId).map(this::mapToDto).orElse(null);
    }

    public Double getCurrentWeight(Long userId) {
        return weightRecordRepository.findTopByUserIdOrderByRecordDateDesc(userId)
                .map(WeightRecord::getWeight)
                .orElse(null);
    }

    public WeightRecordDto updateWeightRecord(Long recordId, Double weight, LocalDate recordDate) {
        WeightRecord record = weightRecordRepository.findById(recordId).orElse(null);
        if (record != null) {
            record.setWeight(weight);
            record.setRecordDate(recordDate);
            return mapToDto(weightRecordRepository.save(record));
        }
        return null;
    }

    public Double getLowestWeight(Long userId) {
        List<WeightRecord> records = weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId);
        return records.stream()
                .map(WeightRecord::getWeight)
                .min(Double::compare)
                .orElse(null);
    }

    public Double getHighestWeight(Long userId) {
        List<WeightRecord> records = weightRecordRepository.findByUserIdOrderByRecordDateAsc(userId);
        return records.stream()
                .map(WeightRecord::getWeight)
                .max(Double::compare)
                .orElse(null);
    }

    private WeightRecordDto mapToDto(WeightRecord record) {
        return WeightRecordDto.builder()
                .id(record.getId())
                .userId(record.getUserId())
                .weight(record.getWeight())
                .recordDate(record.getRecordDate())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
