package com.example.bmimanager.bmi.domain;

import com.example.bmimanager.user.domain.UserFacade;
import com.example.bmimanager.weight.domain.WeightFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BmiConfiguration {

    @Bean(name = "bmiFacadeService")
    public BmiFacade bmiFacade(UserFacade userFacade, WeightFacade weightFacade) {
        return new BmiFacade(userFacade, weightFacade);
    }
}
