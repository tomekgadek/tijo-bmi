package com.example.bmimanager.infrastructure.authentication;

import com.example.bmimanager.user.domain.UserDto;
import com.example.bmimanager.user.domain.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
class AuthenticationConfiguration {

    private final UserFacade userFacade;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationConfiguration(UserFacade userFacade, BCryptPasswordEncoder passwordEncoder) {
        this.userFacade = userFacade;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDto user = userFacade.findByUsername(username)
                    .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                            "Użytkownik nie znaleziony: " + username));

            String role = username.equals("admin") ? "ROLE_ADMIN" : "ROLE_USER";

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(role))
                    .accountExpired(false)
                    .accountLocked(user.getIsBlocked())
                    .credentialsExpired(false)
                    .disabled(user.getIsBlocked())
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
