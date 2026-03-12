package com.example.bmimanager.bmi.domain;

import com.example.bmimanager.user.domain.UserFacade;
import com.example.bmimanager.weight.domain.WeightFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BmiConfiguration {

    BmiFacade bmiFacade() {
        return bmiFacade(
                new com.example.bmimanager.user.domain.UserConfiguration().userFacade(),
                new com.example.bmimanager.weight.domain.WeightConfiguration().weightFacade());
    }

    @Bean
    public BmiFacade bmiFacade(UserFacade userFacade, WeightFacade weightFacade) {
        return new BmiFacade(userFacade, weightFacade);
    }
}
