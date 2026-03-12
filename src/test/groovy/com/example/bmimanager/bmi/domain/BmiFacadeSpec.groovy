package com.example.bmimanager.bmi.domain

import com.example.bmimanager.user.domain.UserDto
import com.example.bmimanager.user.domain.UserFacade
import com.example.bmimanager.weight.domain.WeightFacade
import com.example.bmimanager.weight.domain.WeightRecordDto
import spock.lang.Specification

import java.util.Optional

class BmiFacadeSpec extends Specification {

    def bmiFacade = new BmiConfiguration().bmiFacade()

    def "should calculate and return correct BMI statistics for existing user"() {
        given: "an existing user with weight records"
        def user = bmiFacade.registerUser("testuser", "testpassword")
        def userId = user.id
        bmiFacade.updateUserProfile(userId, "John", "Doe", 180.0d, true, "Motywacja", "Osiągnięcie")

        bmiFacade.recordWeight(userId, 82.0d, java.time.LocalDate.now().minusDays(2))
        bmiFacade.recordWeight(userId, 75.0d, java.time.LocalDate.now().minusDays(1))
        def latestWeightRecord = bmiFacade.recordWeight(userId, 80.0d, java.time.LocalDate.now())

        when: "BMI statistics are requested"
        def stats = bmiFacade.getBMIStatistics(userId)

        then: "correct BMI statistics are returned based on facades data"
        stats.currentWeight == 80.0d
        stats.lowestWeight == 75.0d
        stats.highestWeight == 82.0d
        stats.recordCount == 3
        stats.latestRecord.weight == latestWeightRecord.weight
        stats.latestRecord.recordDate == latestWeightRecord.recordDate
        stats.currentBMI == 24.691358024691358d // 80 / (1.8 * 1.8)
        stats.category == BMICategory.NORMAL
    }
}
