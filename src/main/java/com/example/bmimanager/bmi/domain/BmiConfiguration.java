package com.example.bmimanager.bmi.domain;

import com.example.bmimanager.user.domain.UserService;
import com.example.bmimanager.weight.domain.WeightFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BmiConfiguration {

    @Bean(name = "bmiFacadeService")
    public BmiFacade bmiFacade(UserService userService, WeightFacade weightFacade) {
        return new BmiFacade(userService, weightFacade);
    }
}
