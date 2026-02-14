package com.example.bmimanager.controller;

import com.example.bmimanager.dto.PublicProfileDto;
import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.entity.WeightRecord;
import com.example.bmimanager.service.BMIFacadeService;
import com.example.bmimanager.service.UserService;
import com.example.bmimanager.service.WeightService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/public")
public class PublicProfileController {

    private final UserService userService;
    private final WeightService weightService;
    private final BMIFacadeService bmiFacadeService;

    public PublicProfileController(UserService userService, WeightService weightService,
            BMIFacadeService bmiFacadeService) {
        this.userService = userService;
        this.weightService = weightService;
        this.bmiFacadeService = bmiFacadeService;
    }

    @GetMapping("/profiles")
    public String viewPublicProfiles(Model model) {
        List<BmiUser> publicBmiUsers = bmiFacadeService.getPublicProfiles();
        List<PublicProfileDto> profiles = publicBmiUsers.stream()
                .map(user -> new PublicProfileDto(
                        user,
                        weightService.getCurrentWeight(user),
                        weightService.getCurrentBMI(user)))
                .collect(Collectors.toList());

        model.addAttribute("profiles", profiles);
        return "public-profiles";
    }

    @GetMapping("/profile/{userId}")
    public String viewUserProfile(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            Model model) {
        BmiUser bmiUser = userService.getUserById(userId);

        if (bmiUser == null || !bmiUser.getIsPublic()) {
            return "redirect:/public/profiles";
        }

        Page<WeightRecord> paginatedHistory = bmiFacadeService.getPaginatedUserWeightHistory(userId, page, size);
        BMIFacadeService.BMIStatistics stats = bmiFacadeService.getBMIStatistics(userId);

        // Przygotowanie danych do wygenerowania wykresu
        List<WeightRecord> currentSlice = paginatedHistory.getContent();
        List<WeightRecord> chronologicalSlice = currentSlice.stream()
                .sorted(Comparator.comparing(WeightRecord::getRecordDate))
                .toList();

        List<String> chartLabels = chronologicalSlice.stream()
                .map(r -> r.getRecordDate().toString())
                .collect(Collectors.toList());
        List<Double> chartData = chronologicalSlice.stream()
                .map(WeightRecord::getWeight)
                .collect(Collectors.toList());

        model.addAttribute("profileUser", bmiUser);
        model.addAttribute("weightHistory", paginatedHistory.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedHistory.getTotalPages());
        model.addAttribute("totalItems", paginatedHistory.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("stats", stats);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "public-profile-view";
    }
}
