package com.example.bmimanager.controller;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.entity.WeightRecord;
import com.example.bmimanager.service.BMIFacadeService;
import com.example.bmimanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final BMIFacadeService bmiFacadeService;

    public AdminController(UserService userService, BMIFacadeService bmiFacadeService) {
        this.userService = userService;
        this.bmiFacadeService = bmiFacadeService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        List<BmiUser> allBmiUsers = userService.getAllUsers();

        // Prepare data for the chart - handle null BMIs by converting them to 0 or
        // omitting
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
        List<BmiUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/user/{userId}")
    public String viewUserDetails(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        BmiUser user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/admin/users";
        }

        Page<WeightRecord> paginatedHistory = bmiFacadeService.getPaginatedUserWeightHistory(userId, page, size);
        BMIFacadeService.BMIStatistics stats = bmiFacadeService.getBMIStatistics(userId);

        // Prepare chart data
        List<WeightRecord> currentSlice = paginatedHistory.getContent();
        List<WeightRecord> chronologicalSlice = currentSlice.stream()
                .sorted(java.util.Comparator.comparing(WeightRecord::getRecordDate))
                .toList();

        List<String> chartLabels = chronologicalSlice.stream()
                .map(r -> r.getRecordDate().toString())
                .collect(Collectors.toList());
        List<Double> chartData = chronologicalSlice.stream()
                .map(WeightRecord::getWeight)
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("weightHistory", paginatedHistory.getContent());
        model.addAttribute("stats", stats);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedHistory.getTotalPages());
        model.addAttribute("totalItems", paginatedHistory.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "admin/user-details";
    }

    @GetMapping("/user/block/{userId}")
    public String blockUser(@PathVariable Long userId, Authentication authentication) {
        userService.findByUsername(authentication.getName()).ifPresent(currentUser -> {
            if (!currentUser.getId().equals(userId)) {
                BmiUser user = userService.getUserById(userId);
                if (user != null) {
                    user.setIsBlocked(true);
                    userService.saveUser(user);
                }
            }
        });
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/user/unblock/{userId}")
    public String unblockUser(@PathVariable Long userId, Authentication authentication) {
        userService.findByUsername(authentication.getName()).ifPresent(currentUser -> {
            if (!currentUser.getId().equals(userId)) {
                BmiUser user = userService.getUserById(userId);
                if (user != null) {
                    user.setIsBlocked(false);
                    userService.saveUser(user);
                }
            }
        });
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, Authentication authentication) {
        userService.findByUsername(authentication.getName()).ifPresent(currentUser -> {
            if (!currentUser.getId().equals(userId)) {
                userService.deleteUser(userId);
            }
        });
        return "redirect:/admin/dashboard";
    }
}
