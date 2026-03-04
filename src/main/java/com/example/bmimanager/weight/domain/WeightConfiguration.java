package com.example.bmimanager.weight.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeightConfiguration {

    @Bean
    public WeightFacade weightFacade(WeightRecordRepository weightRecordRepository) {
        return new WeightFacade(weightRecordRepository);
    }
}
