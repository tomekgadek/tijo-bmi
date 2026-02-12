package com.example.bmimanager.service;

import com.example.bmimanager.entity.User;
import com.example.bmimanager.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username już istnieje");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .isPublic(false)
                .isBlocked(false)
                .resultsPerPage(25)
                .build();
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long userId, String firstName, String lastName, Double height, Boolean isPublic,
            String motivationalQuote, String achievement) {
        User user = getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Użytkownik nie znaleziony");
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setHeight(height);
        user.setIsPublic(isPublic);
        user.setMotivationalQuote(motivationalQuote);
        user.setAchievement(achievement);

        return userRepository.save(user);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<User> getPublicProfiles() {
        return userRepository.findByIsPublicTrue();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
