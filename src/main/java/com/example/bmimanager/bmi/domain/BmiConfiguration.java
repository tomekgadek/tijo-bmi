package com.example.bmimanager.bmi.domain;

import com.example.bmimanager.user.domain.UserService;
import com.example.bmimanager.profile.domain.WeightService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BmiConfiguration {

    @Bean(name = "bmiFacadeService")
    public BmiFacade bmiFacade(UserService userService, WeightService weightService) {
        return new BmiFacade(userService, weightService);
    }
}
