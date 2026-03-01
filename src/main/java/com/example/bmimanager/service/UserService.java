package com.example.bmimanager.service;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.repository.BmiUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final BmiUserRepository bmiUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final org.springframework.context.MessageSource messageSource;

    public UserService(BmiUserRepository bmiUserRepository, BCryptPasswordEncoder passwordEncoder,
            org.springframework.context.MessageSource messageSource) {
        this.bmiUserRepository = bmiUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    public BmiUser registerUser(String username, String password) {
        if (bmiUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("validation.username.exists", null,
                    org.springframework.context.i18n.LocaleContextHolder.getLocale()));
        }

        BmiUser bmiUser = BmiUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .isPublic(false)
                .isBlocked(false)
                .resultsPerPage(25)
                .build();
        return bmiUserRepository.save(bmiUser);
    }

    public Optional<BmiUser> findByUsername(String username) {
        return bmiUserRepository.findByUsername(username);
    }

    public BmiUser getUserById(Long id) {
        return bmiUserRepository.findById(id).orElse(null);
    }

    public BmiUser updateUser(Long userId, String firstName, String lastName, Double height, Boolean isPublic,
            String motivationalQuote, String achievement) {
        BmiUser user = getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException(messageSource.getMessage("validation.user.notfound", null,
                    org.springframework.context.i18n.LocaleContextHolder.getLocale()));
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setHeight(height);
        user.setIsPublic(isPublic);
        user.setMotivationalQuote(motivationalQuote);
        user.setAchievement(achievement);

        return bmiUserRepository.save(user);
    }

    public List<BmiUser> getAllUsers() {
        return bmiUserRepository.findAll();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<BmiUser> getPublicProfiles() {
        return bmiUserRepository.findByIsPublicTrueAndIsBlockedFalse();
    }

    public void saveUser(BmiUser user) {
        bmiUserRepository.save(user);
    }

    public void deleteUser(Long userId) {
        bmiUserRepository.deleteById(userId);
    }
}
