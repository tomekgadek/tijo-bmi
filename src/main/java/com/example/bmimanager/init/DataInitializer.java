/*
 * package com.example.bmimanager.init;
 * 
 * import com.example.bmimanager.entity.User;
 * import com.example.bmimanager.entity.WeightRecord;
 * import com.example.bmimanager.repository.UserRepository;
 * import com.example.bmimanager.repository.WeightRecordRepository;
 * import org.springframework.boot.CommandLineRunner;
 * import org.springframework.context.annotation.Bean;
 * import org.springframework.context.annotation.Configuration;
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * 
 * import java.time.LocalDate;
 * 
 * @Configuration
 * public class DataInitializer {
 * 
 * @Bean
 * CommandLineRunner initDatabase(UserRepository userRepository,
 * WeightRecordRepository weightRecordRepository) {
 * return args -> {
 * BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
 * 
 * // Create admin user
 * User admin = new User("admin", encoder.encode("admin123"), "Admin", "System",
 * 175.0);
 * admin.setIsPublic(true);
 * admin.setMotivationalQuote("Adminuj mądrze, pomagaj użytkownikom!");
 * admin.setAchievement("Administrator BMI Manager");
 * admin = userRepository.save(admin);
 * 
 * // Add weight records for admin
 * weightRecordRepository.save(new WeightRecord(admin, 80.0,
 * LocalDate.now().minusDays(30)));
 * weightRecordRepository.save(new WeightRecord(admin, 79.0,
 * LocalDate.now().minusDays(20)));
 * weightRecordRepository.save(new WeightRecord(admin, 78.0,
 * LocalDate.now().minusDays(10)));
 * weightRecordRepository.save(new WeightRecord(admin, 77.5, LocalDate.now()));
 * 
 * // Create demo user
 * User user1 = new User("demo", encoder.encode("demo123"), "Jan", "Kowalski",
 * 180.0);
 * user1.setIsPublic(true);
 * user1.
 * setMotivationalQuote("Każdy dzień to nowa szansa na zdrowszy styl życia!");
 * user1.setAchievement("Stracił kilkanaście kg w pół roku!");
 * user1 = userRepository.save(user1);
 * 
 * // Add weight records for demo user
 * // Previously generated here, now moved to data.sql
 * };
 * }
 * }
 */
