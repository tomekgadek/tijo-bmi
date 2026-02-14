package com.example.bmimanager.controller;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.entity.WeightRecord;
import com.example.bmimanager.service.BMIFacadeService;
import com.example.bmimanager.service.UserService;
import com.example.bmimanager.service.WeightService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final WeightService weightService;
    private final BMIFacadeService bmiFacadeService;

    public AdminController(UserService userService, WeightService weightService, BMIFacadeService bmiFacadeService) {
        this.userService = userService;
        this.weightService = weightService;
        this.bmiFacadeService = bmiFacadeService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/profile";
        }

        List<BmiUser> allBmiUsers = userService.getUserRepository().findAll();

        // Prepare data for the chart - handle null BMIs by converting them to 0 or omitting
        List<String> usernames = allBmiUsers.stream().map(BmiUser::getUsername).collect(Collectors.toList());
        List<Double> currentBMIs = allBmiUsers.stream()
                .map(user -> {
                    Double bmi = bmiFacadeService.getUserCurrentBMI(user.getId());
                    return bmi != null ? (double) Math.round(bmi * 10) / 10 : 0.0;
                })
                .collect(Collectors.toList());

        long publicCount = allBmiUsers.stream().filter(BmiUser::getIsPublic).count();
        long privateCount = allBmiUsers.size() - publicCount;

        model.addAttribute("users", allBmiUsers);
        model.addAttribute("usernames", usernames);
        model.addAttribute("currentBMIs", currentBMIs);
        model.addAttribute("publicCount", publicCount);
        model.addAttribute("privateCount", privateCount);

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<BmiUser> users = userService.getUserRepository().findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/user/{userId}")
    public String viewUserDetails(@PathVariable Long userId, Model model) {
        BmiUser user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/admin/users";
        }

        List<WeightRecord> weightHistory = weightService.getUserWeightRecords(user);
        BMIFacadeService.BMIStatistics stats = bmiFacadeService.getBMIStatistics(userId);

        model.addAttribute("user", user);
        model.addAttribute("weightHistory", weightHistory);
        model.addAttribute("stats", stats);

        return "admin/user-details";
    }

    @GetMapping("/user/block/{userId}")
    public String blockUser(@PathVariable Long userId) {
        BmiUser user = userService.getUserById(userId);
        if (user != null) {
            user.setIsBlocked(true);
            userService.getUserRepository().save(user);
        }
        return "redirect:/admin/user/" + userId;
    }

    @GetMapping("/user/unblock/{userId}")
    public String unblockUser(@PathVariable Long userId) {
        BmiUser user = userService.getUserById(userId);
        if (user != null) {
            user.setIsBlocked(false);
            userService.getUserRepository().save(user);
        }
        return "redirect:/admin/user/" + userId;
    }
}
