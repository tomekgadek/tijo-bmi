package com.example.bmimanager.user.domain;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserConfiguration {

    @Bean
    public UserFacade userFacade(BmiUserRepository bmiUserRepository,
            BCryptPasswordEncoder passwordEncoder,
            MessageSource messageSource) {
        return new UserFacade(bmiUserRepository, passwordEncoder, messageSource);
    }
}
