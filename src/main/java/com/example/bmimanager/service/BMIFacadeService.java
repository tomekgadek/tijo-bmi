package com.example.bmimanager.service;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.entity.WeightRecord;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * BMIFacadeService - Pattern Fasada
 * Agreguje logikę biznesową związaną z BMI
 * Ukrywa złożoność poszczególnych serwisów
 */
@Service
public class BMIFacadeService {

    private final UserService userService;
    private final WeightService weightService;

    public BMIFacadeService(UserService userService, WeightService weightService) {
        this.userService = userService;
        this.weightService = weightService;
    }

    /**
     * Rejestruje nowego użytkownika tylko z loginem i hasłem
     */
    public BmiUser registerUser(String username, String password) {
        return userService.registerUser(username, password);
    }

    /**
     * Zwraca aktualny BMI użytkownika
     */
    public Double getUserCurrentBMI(Long userId) {
        BmiUser bmiUser = userService.getUserById(userId);
        if (bmiUser == null)
            return null;
        return weightService.getCurrentBMI(bmiUser);
    }

    /**
     * Zwraca historię wag wraz z obliczonym BMI
     */
    public List<WeightRecord> getUserWeightHistory(Long userId) {
        BmiUser bmiUser = userService.getUserById(userId);
        if (bmiUser == null)
            return List.of();
        return weightService.getUserWeightRecords(bmiUser);
    }

    public Page<WeightRecord> getPaginatedUserWeightHistory(Long userId, int page, int size) {
        BmiUser bmiUser = userService.getUserById(userId);
        return bmiUser != null ? weightService.getPaginatedUserWeightRecords(bmiUser, page, size) : Page.empty();
    }

    /**
     * Dodaje nowy zapis wagi
     */
    public WeightRecord recordWeight(Long userId, Double weight) {
        BmiUser user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Użytkownik nie znaleziony");
        }
        return weightService.addWeightRecord(user, weight);
    }

    /**
     * Dodaje nowy zapis wagi z datą
     */
    public WeightRecord recordWeight(Long userId, Double weight, LocalDate recordDate) {
        BmiUser user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Użytkownik nie znaleziony");
        }
        return weightService.addWeightRecord(user, weight, recordDate);
    }

    /**
     * Zwraca statystyki BMI
     */
    public BMIStatistics getBMIStatistics(Long userId) {
        BmiUser user = userService.getUserById(userId);
        if (user == null)
            return null; // TODO: pasuje coś zrobić z tym zwracanym null :(

        Double currentWeight = weightService.getCurrentWeight(user);
        Double currentBMI = weightService.getCurrentBMI(user);
        Double lowestWeight = weightService.getLowestWeight(user);
        Double highestWeight = weightService.getHighestWeight(user);
        List<WeightRecord> records = weightService.getUserWeightRecords(user);

        // TODO: nie podoba mi się ta składnia
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

    /**
     * Zwraca kategorie BMI
     */
    public String getBMICategory(Double bmi) {
        // TODO: do zmiany, pasuje użyć enum
        if (bmi == null || bmi == 0)
            return "Brak danych";
        if (bmi < 18.5)
            return "Niedowaga";
        if (bmi < 25)
            return "Prawidłowa waga";
        if (bmi < 30)
            return "Nadwaga";
        return "Otyłość";
    }

    /**
     * Aktualizuje profil użytkownika
     */
    public BmiUser updateUserProfile(Long userId, String firstName, String lastName, Double height,
                                     Boolean isPublic, String motivationalQuote, String achievement) {
        return userService.updateUser(userId, firstName, lastName, height, isPublic, motivationalQuote, achievement);
    }

    /**
     * Zwraca publiczne profile
     */
    public List<BmiUser> getPublicProfiles() {
        return userService.getPublicProfiles();
    }

    /**
     * Usuwa zapis wagi
     */
    public void deleteWeightRecord(Long userId, Long recordId) {
        WeightRecord record = weightService.getWeightRecordById(recordId);
        if (record != null && record.getBmiUser().getId().equals(userId)) {
            weightService.deleteWeightRecord(recordId);
        }
    }

    /**
     * Pomocna klasa do przechowywania statystyk BMI
     */
    public static class BMIStatistics {
        public Double currentWeight;
        public Double currentBMI;
        public Double lowestWeight;
        public Double highestWeight;
        public Integer recordCount;
        public String category;
        public WeightRecord latestRecord;

        public BMIStatistics(Double currentWeight, Double currentBMI, Double lowestWeight, Double highestWeight,
                Integer recordCount, String category, WeightRecord latestRecord) {
            this.currentWeight = currentWeight;
            this.currentBMI = currentBMI;
            this.lowestWeight = lowestWeight;
            this.highestWeight = highestWeight;
            this.recordCount = recordCount;
            this.category = category;
            this.latestRecord = latestRecord;
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

        public String getCategory() {
            return category;
        }
    }
}
