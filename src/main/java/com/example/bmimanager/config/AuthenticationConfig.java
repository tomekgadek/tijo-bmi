package com.example.bmimanager.config;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.service.UserService;
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
public class AuthenticationConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationConfig(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            BmiUser user = userService.findByUsername(username)
                    .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                            "Użytkownik nie znaleziony: " + username));

            // Zamiast przypisywać role bezpośrednio, możesz sprawdzić czy użytkownik to admin
            // Na potrzeby tego projektu admin to użytkownik o username "admin"
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
