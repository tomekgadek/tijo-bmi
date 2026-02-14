package com.example.bmimanager.service;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.entity.WeightRecord;
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

    public WeightRecord addWeightRecord(BmiUser bmiUser, Double weight) {
        WeightRecord record =  WeightRecord.builder()
                .bmiUser(bmiUser)
                .weight(weight)
                .recordDate(LocalDate.now())
                .build();
        return weightRecordRepository.save(record);
    }

    public WeightRecord addWeightRecord(BmiUser bmiUser, Double weight, LocalDate recordDate) {
        WeightRecord record =  WeightRecord.builder()
                .bmiUser(bmiUser)
                .weight(weight)
                .recordDate(LocalDate.now())
                .build();
        return weightRecordRepository.save(record);
    }

    public List<WeightRecord> getUserWeightRecords(BmiUser bmiUser) {
        return weightRecordRepository.findByBmiUserOrderByRecordDateAsc(bmiUser);
    }

    public Page<WeightRecord> getPaginatedUserWeightRecords(BmiUser bmiUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return weightRecordRepository.findByBmiUserOrderByRecordDateDesc(bmiUser, pageable);
    }

    public List<WeightRecord> getUserWeightRecordsByDateRange(BmiUser bmiUser, LocalDate startDate, LocalDate endDate) {
        return weightRecordRepository.findByUserAndDateRange(bmiUser, startDate, endDate);
    }

    public void deleteWeightRecord(Long recordId) {
        weightRecordRepository.deleteById(recordId);
    }

    public WeightRecord getWeightRecordById(Long recordId) {
        return weightRecordRepository.findById(recordId).orElse(null);
    }

    public Double getCurrentBMI(BmiUser user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        if (!records.isEmpty()) {
            return records.getLast().calculateBMI();
        }
        return null;
    }

    public Double getCurrentWeight(BmiUser user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        if (!records.isEmpty()) {
            return records.getLast().getWeight();
        }
        return null;
    }

    // TODO: zweryfikować wszystkie nieużywane metody i ostatecznie je usunąć
    public WeightRecord updateWeightRecord(Long recordId, Double weight, LocalDate recordDate) {
        WeightRecord record = getWeightRecordById(recordId);
        if (record != null) {
            record.setWeight(weight);
            record.setRecordDate(recordDate);
            return weightRecordRepository.save(record);
        }
        return null;
    }

    public Double getLowestWeight(BmiUser user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        return records.stream()
                .map(WeightRecord::getWeight)
                .min(Double::compare)
                .orElse(null);
    }

    public Double getHighestWeight(BmiUser user) {
        List<WeightRecord> records = getUserWeightRecords(user);
        return records.stream()
                .map(WeightRecord::getWeight)
                .max(Double::compare)
                .orElse(null);
    }
}
