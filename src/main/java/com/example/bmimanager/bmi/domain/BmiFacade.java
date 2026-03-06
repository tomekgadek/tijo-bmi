package com.example.bmimanager.bmi.domain;

import com.example.bmimanager.user.domain.UserDto;
import com.example.bmimanager.user.domain.UserFacade;
import com.example.bmimanager.weight.domain.WeightFacade;
import com.example.bmimanager.weight.domain.WeightRecordDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public class BmiFacade {

    private final UserFacade userFacade;
    private final WeightFacade weightFacade;

    public BmiFacade(UserFacade userFacade, WeightFacade weightFacade) {
        this.userFacade = userFacade;
        this.weightFacade = weightFacade;
    }

    public UserDto registerUser(String username, String password) {
        return userFacade.registerUser(username, password);
    }

    public Double getUserCurrentBMI(Long userId) {
        UserDto user = userFacade.getUserById(userId);
        if (user == null)
            return null;
        Double currentWeight = weightFacade.getCurrentWeight(userId);
        return BmiCalculator.calculate(currentWeight, user.getHeight());
    }

    public Double getUserCurrentWeight(Long userId) {
        return weightFacade.getCurrentWeight(userId);
    }

    public List<WeightRecordDto> getUserWeightHistory(Long userId) {
        return weightFacade.getUserWeightRecords(userId);
    }

    public Page<WeightRecordDto> getPaginatedUserWeightHistory(Long userId, int page, int size) {
        return weightFacade.getPaginatedUserWeightRecords(userId, page, size);
    }

    public WeightRecordDto recordWeight(Long userId, Double weight) {
        return weightFacade.addWeightRecord(userId, weight);
    }

    public WeightRecordDto recordWeight(Long userId, Double weight, LocalDate recordDate) {
        return weightFacade.addWeightRecord(userId, weight, recordDate);
    }

    public BMIStatistics getBMIStatistics(Long userId) {
        UserDto user = userFacade.getUserById(userId);
        if (user == null)
            return BMIStatistics.empty();

        Double currentWeight = weightFacade.getCurrentWeight(userId);
        Double currentBMI = BmiCalculator.calculate(currentWeight, user.getHeight());
        Double lowestWeight = weightFacade.getLowestWeight(userId);
        Double highestWeight = weightFacade.getHighestWeight(userId);

        List<WeightRecordDto> records = weightFacade.getUserWeightRecords(userId);
        WeightRecordDto latestRecord = records.isEmpty() ? null : records.get(records.size() - 1);

        return new BMIStatistics(
                currentWeight,
                currentBMI,
                lowestWeight,
                highestWeight,
                records.size(),
                getBMICategory(currentBMI),
                latestRecord);
    }

    public BMICategory getBMICategory(Double bmi) {
        if (bmi == null || bmi == 0)
            return BMICategory.NO_DATA;
        if (bmi < 18.5)
            return BMICategory.UNDERWEIGHT;
        if (bmi < 25)
            return BMICategory.NORMAL;
        if (bmi < 30)
            return BMICategory.OVERWEIGHT;
        return BMICategory.OBESITY;
    }

    public UserDto updateUserProfile(Long userId, String firstName, String lastName, Double height,
            Boolean isPublic, String motivationalQuote, String achievement) {
        return userFacade.updateUser(userId, firstName, lastName, height, isPublic, motivationalQuote, achievement);
    }

    public List<UserDto> getPublicProfiles() {
        return userFacade.getPublicProfiles();
    }

    public void deleteWeightRecord(Long userId, Long recordId) {
        WeightRecordDto record = weightFacade.getWeightRecordById(recordId);
        if (record != null && record.getUserId().equals(userId)) {
            weightFacade.deleteWeightRecord(recordId);
        }
    }

    public WeightRecordDto updateWeightRecord(Long userId, Long recordId, Double weight) {
        WeightRecordDto record = weightFacade.getWeightRecordById(recordId);
        if (record != null && record.getUserId().equals(userId)) {
            return weightFacade.updateWeightRecord(recordId, weight, record.getRecordDate());
        }
        return null;
    }

    public static class BMIStatistics {
        private final Double currentWeight;
        private final Double currentBMI;
        private final Double lowestWeight;
        private final Double highestWeight;
        private final Integer recordCount;
        private final BMICategory category;
        private final WeightRecordDto latestRecord;

        public BMIStatistics(Double currentWeight, Double currentBMI, Double lowestWeight, Double highestWeight,
                Integer recordCount, BMICategory category, WeightRecordDto latestRecord) {
            this.currentWeight = currentWeight;
            this.currentBMI = currentBMI;
            this.lowestWeight = lowestWeight;
            this.highestWeight = highestWeight;
            this.recordCount = recordCount;
            this.category = category;
            this.latestRecord = latestRecord;
        }

        public static BMIStatistics empty() {
            return new BMIStatistics(null, null, null, null, 0, BMICategory.NO_DATA, null);
        }

        public Double getCurrentWeight() {
            return currentWeight;
        }

        public Double getCurrentBMI() {
            return currentBMI;
        }

        public WeightRecordDto getLatestRecord() {
            return latestRecord;
        }

        public Double getLowestWeight() {
            return lowestWeight;
        }

        public Double getHighestWeight() {
            return highestWeight;
        }

        public Integer getRecordCount() {
            return recordCount;
        }

        public BMICategory getCategory() {
            return category;
        }
    }
}
