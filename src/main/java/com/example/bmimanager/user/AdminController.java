package com.example.bmimanager.user;

import com.example.bmimanager.user.domain.UserDto;
import com.example.bmimanager.weight.domain.WeightRecordDto;
import com.example.bmimanager.bmi.domain.BmiFacade;
import com.example.bmimanager.user.domain.UserFacade;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserFacade userFacade;
    private final BmiFacade bmiFacade;

    public AdminController(UserFacade userFacade, BmiFacade bmiFacade) {
        this.userFacade = userFacade;
        this.bmiFacade = bmiFacade;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        List<UserDto> allBmiUsers = userFacade.getAllUsers();

        List<String> usernames = allBmiUsers.stream().map(UserDto::getUsername).collect(Collectors.toList());
        List<Double> currentBMIs = allBmiUsers.stream()
                .map(user -> bmiFacade.getUserCurrentBMI(user.getId())
                        .map(bmi -> (double) Math.round(bmi * 10) / 10)
                        .orElse(0.0))
                .collect(Collectors.toList());

        long publicCount = allBmiUsers.stream().filter(UserDto::getIsPublic).count();
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
        List<UserDto> users = userFacade.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/user/{userId}")
    public String viewUserDetails(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Optional<UserDto> userOpt = userFacade.getUserById(userId);
        if (userOpt.isEmpty()) {
            return "redirect:/admin/users";
        }
        UserDto user = userOpt.get();

        Page<WeightRecordDto> paginatedHistory = bmiFacade.getPaginatedUserWeightHistory(userId, page, size);
        BmiFacade.BMIStatistics stats = bmiFacade.getBMIStatistics(userId);

        List<WeightRecordDto> currentSlice = paginatedHistory.getContent();
        List<WeightRecordDto> chronologicalSlice = currentSlice.stream()
                .sorted(java.util.Comparator.comparing(WeightRecordDto::getRecordDate))
                .toList();

        List<String> chartLabels = chronologicalSlice.stream()
                .map(r -> r.getRecordDate().toString())
                .collect(Collectors.toList());
        List<Double> chartData = chronologicalSlice.stream()
                .map(WeightRecordDto::getWeight)
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
        Optional<UserDto> currentUserOpt = userFacade.findByUsername(authentication.getName());
        if (currentUserOpt.isPresent() && !currentUserOpt.get().getId().equals(userId)) {
            Optional<UserDto> userToBlockOpt = userFacade.getUserById(userId);
            if (userToBlockOpt.isPresent()) {
                // Logic for blocking can be added here
            }
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/user/unblock/{userId}")
    public String unblockUser(@PathVariable Long userId, Authentication authentication) {
        Optional<UserDto> currentUserOpt = userFacade.findByUsername(authentication.getName());
        if (currentUserOpt.isPresent() && !currentUserOpt.get().getId().equals(userId)) {
            Optional<UserDto> userToUnblockOpt = userFacade.getUserById(userId);
            if (userToUnblockOpt.isPresent()) {
                // Logic for unblocking can be added here
            }
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, Authentication authentication) {
        Optional<UserDto> currentUserOpt = userFacade.findByUsername(authentication.getName());
        if (currentUserOpt.isPresent() && !currentUserOpt.get().getId().equals(userId)) {
            userFacade.deleteUser(userId);
        }
        return "redirect:/admin/dashboard";
    }
}
