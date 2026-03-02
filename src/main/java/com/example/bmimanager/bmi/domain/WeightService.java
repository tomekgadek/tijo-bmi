package com.example.bmimanager.bmi.domain;

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
        WeightRecord record = WeightRecord.builder()
                .bmiUser(bmiUser)
                .weight(weight)
                .recordDate(LocalDate.now())
                .build();
        return weightRecordRepository.save(record);
    }

    public WeightRecord addWeightRecord(BmiUser bmiUser, Double weight, LocalDate recordDate) {
        WeightRecord record = WeightRecord.builder()
                .bmiUser(bmiUser)
                .weight(weight)
                .recordDate(recordDate)
                .build();
        return weightRecordRepository.save(record);
    }

    public List<WeightRecord> getUserWeightRecords(BmiUser bmiUser) {
        return weightRecordRepository.findByBmiUserOrderByRecordDateAsc(bmiUser);
    }

    public Page<WeightRecord> getPaginatedUserWeightRecords(BmiUser bmiUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return weightRecordRepository.findByBmiUserOrderByRecordDateAsc(bmiUser, pageable);
    }

    public void deleteWeightRecord(Long recordId) {
        weightRecordRepository.deleteById(recordId);
    }

    public WeightRecord getWeightRecordById(Long recordId) {
        return weightRecordRepository.findById(recordId).orElse(null);
    }

    public Double getCurrentBMI(BmiUser user) {
        return weightRecordRepository.findTopByBmiUserOrderByRecordDateDesc(user)
                .map(record -> BmiCalculator.calculate(record.getWeight(), user.getHeight()))
                .orElse(null);
    }

    public Double getCurrentWeight(BmiUser user) {
        return weightRecordRepository.findTopByBmiUserOrderByRecordDateDesc(user)
                .map(WeightRecord::getWeight)
                .orElse(null);
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
