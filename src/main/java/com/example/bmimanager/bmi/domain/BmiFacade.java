package com.example.bmimanager.bmi.domain;

import com.example.bmimanager.user.domain.BmiUser;
import com.example.bmimanager.profile.domain.WeightRecord;
import com.example.bmimanager.user.domain.UserService;
import com.example.bmimanager.profile.domain.WeightService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BmiFacade {

    private final UserService userService;
    private final WeightService weightService;

    public BmiFacade(UserService userService, WeightService weightService) {
        this.userService = userService;
        this.weightService = weightService;
    }

    public BmiUser registerUser(String username, String password) {
        return userService.registerUser(username, password);
    }

    public Double getUserCurrentBMI(Long userId) {
        BmiUser bmiUser = userService.getUserById(userId);
        return bmiUser != null ? weightService.getCurrentBMI(bmiUser) : null;
    }

    public List<WeightRecord> getUserWeightHistory(Long userId) {
        BmiUser bmiUser = userService.getUserById(userId);
        return bmiUser != null ? weightService.getUserWeightRecords(bmiUser) : List.of();
    }

    public Page<WeightRecord> getPaginatedUserWeightHistory(Long userId, int page, int size) {
        BmiUser bmiUser = userService.getUserById(userId);
        return bmiUser != null ? weightService.getPaginatedUserWeightRecords(bmiUser, page, size) : Page.empty();
    }

    public WeightRecord recordWeight(Long userId, Double weight) {
        BmiUser user = userService.getUserById(userId);
        if (user == null)
            throw new IllegalArgumentException("Użytkownik nie znaleziony");
        return weightService.addWeightRecord(user, weight);
    }

    public WeightRecord recordWeight(Long userId, Double weight, LocalDate recordDate) {
        BmiUser user = userService.getUserById(userId);
        if (user == null)
            throw new IllegalArgumentException("Użytkownik nie znaleziony");
        return weightService.addWeightRecord(user, weight, recordDate);
    }

    public BMIStatistics getBMIStatistics(Long userId) {
        BmiUser user = userService.getUserById(userId);
        if (user == null)
            return BMIStatistics.empty();

        Double currentWeight = weightService.getCurrentWeight(user);
        Double currentBMI = weightService.getCurrentBMI(user);
        Double lowestWeight = weightService.getLowestWeight(user);
        Double highestWeight = weightService.getHighestWeight(user);

        List<WeightRecord> records = weightService.getUserWeightRecords(user);
        WeightRecord latestRecord = records.isEmpty() ? null : records.get(records.size() - 1);

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

    public BmiUser updateUserProfile(Long userId, String firstName, String lastName, Double height,
            Boolean isPublic, String motivationalQuote, String achievement) {
        return userService.updateUser(userId, firstName, lastName, height, isPublic, motivationalQuote, achievement);
    }

    public List<BmiUser> getPublicProfiles() {
        return userService.getPublicProfiles();
    }

    public void deleteWeightRecord(Long userId, Long recordId) {
        WeightRecord record = weightService.getWeightRecordById(recordId);
        if (record != null && record.getBmiUser().getId().equals(userId)) {
            weightService.deleteWeightRecord(recordId);
        }
    }

    public WeightRecord updateWeightRecord(Long userId, Long recordId, Double weight) {
        WeightRecord record = weightService.getWeightRecordById(recordId);
        if (record != null && record.getBmiUser().getId().equals(userId)) {
            return weightService.updateWeightRecord(recordId, weight, record.getRecordDate());
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
        private final WeightRecord latestRecord;

        public BMIStatistics(Double currentWeight, Double currentBMI, Double lowestWeight, Double highestWeight,
                Integer recordCount, BMICategory category, WeightRecord latestRecord) {
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

        public WeightRecord getLatestRecord() {
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
