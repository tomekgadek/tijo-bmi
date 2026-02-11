package com.example.bmimanager.service;

import com.example.bmimanager.entity.WeightRecord;
import com.example.bmimanager.entity.User;
import com.example.bmimanager.repository.WeightRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeightService {

    private final WeightRecordRepository weightRecordRepository;

    public WeightService(WeightRecordRepository weightRecordRepository) {
        this.weightRecordRepository = weightRecordRepository;
    }

    public WeightRecord addWeightRecord(User user, Double weight) {
        WeightRecord record = new WeightRecord(user, weight, LocalDate.now());
        return weightRecordRepository.save(record);
    }

    public WeightRecord addWeightRecord(User user, Double weight, LocalDate recordDate) {
        WeightRecord record = new WeightRecord(user, weight, recordDate);
        return weightRecordRepository.save(record);
    }

    public List<WeightRecord> getUserWeightRecords(User user) {
        return weightRecordRepository.findByUserOrderByRecordDateAsc(user);
    }

    public Page<WeightRecord> getPaginatedUserWeightRecords(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return weightRecordRepository.findByUserOrderByRecordDateDesc(user, pageable);
    }

    public List<WeightRecord> getUserWeightRecordsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return weightRecordRepository.findByUserAndDateRange(user, startDate, endDate);
    }

    public void deleteWeightRecord(Long recordId) {
        weightRecordRepository.deleteById(recordId);
    }

    public WeightRecord getWeightRecordById(Long recordId) {
        return weightRecordRepository.findById(recordId).orElse(null);
    }

    public Double getCurrentBMI(User user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        if (!records.isEmpty()) {
            return records.get(records.size() - 1).calculateBMI();
        }
        return null;
    }

    public Double getCurrentWeight(User user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        if (!records.isEmpty()) {
            return records.get(records.size() - 1).getWeight();
        }
        return null;
    }

    public WeightRecord updateWeightRecord(Long recordId, Double weight, LocalDate recordDate) {
        WeightRecord record = getWeightRecordById(recordId);
        if (record != null) {
            record.setWeight(weight);
            record.setRecordDate(recordDate);
            return weightRecordRepository.save(record);
        }
        return null;
    }

    public Double getLowestWeight(User user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        return records.stream()
                .map(WeightRecord::getWeight)
                .min(Double::compare)
                .orElse(null);
    }

    public Double getHighestWeight(User user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        return records.stream()
                .map(WeightRecord::getWeight)
                .max(Double::compare)
                .orElse(null);
    }
}
