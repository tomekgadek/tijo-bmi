package com.example.bmimanager.user.domain;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

public class UserFacade {

    private final BmiUserRepository bmiUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public UserFacade(BmiUserRepository bmiUserRepository, BCryptPasswordEncoder passwordEncoder,
            MessageSource messageSource) {
        this.bmiUserRepository = bmiUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }

    public UserDto registerUser(String username, String password) {
        if (bmiUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("validation.username.exists", null, LocaleContextHolder.getLocale()));
        }

        BmiUser bmiUser = BmiUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .isPublic(false)
                .isBlocked(false)
                .resultsPerPage(25)
                .build();
        return mapToDto(bmiUserRepository.save(bmiUser));
    }

    public Optional<UserDto> findByUsername(String username) {
        return bmiUserRepository.findByUsername(username).map(this::mapToDto);
    }

    public UserDto getUserById(Long id) {
        return bmiUserRepository.findById(id).map(this::mapToDto).orElse(null);
    }

    public UserDto updateUser(Long userId, String firstName, String lastName, Double height, Boolean isPublic,
            String motivationalQuote, String achievement) {
        BmiUser user = bmiUserRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("validation.user.notfound", null, LocaleContextHolder.getLocale()));
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setHeight(height);
        user.setIsPublic(isPublic);
        user.setMotivationalQuote(motivationalQuote);
        user.setAchievement(achievement);

        return mapToDto(bmiUserRepository.save(user));
    }

    public List<UserDto> getAllUsers() {
        return bmiUserRepository.findAll().stream().map(this::mapToDto).toList();
    }

    public List<UserDto> getPublicProfiles() {
        return bmiUserRepository.findByIsPublicTrueAndIsBlockedFalse().stream().map(this::mapToDto).toList();
    }

    public void saveUser(UserDto userDto) {
        bmiUserRepository.findById(userDto.getId()).ifPresent(user -> {
            user.setResultsPerPage(userDto.getResultsPerPage());
            bmiUserRepository.save(user);
        });
    }

    public void deleteUser(Long userId) {
        bmiUserRepository.deleteById(userId);
    }

    private UserDto mapToDto(BmiUser user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .height(user.getHeight())
                .isPublic(user.getIsPublic())
                .isBlocked(user.getIsBlocked())
                .motivationalQuote(user.getMotivationalQuote())
                .achievement(user.getAchievement())
                .resultsPerPage(user.getResultsPerPage())
                .build();
    }
}
