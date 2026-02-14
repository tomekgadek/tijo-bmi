package com.example.bmimanager.controller;

import com.example.bmimanager.entity.BmiUser;
import com.example.bmimanager.entity.WeightRecord;
import com.example.bmimanager.service.BMIFacadeService;
import com.example.bmimanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private final BMIFacadeService bmiFacadeService;
    private final UserService userService;

    public UserProfileController(BMIFacadeService bmiFacadeService, UserService userService) {
        this.bmiFacadeService = bmiFacadeService;
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            Authentication authentication, Model model) {
        String username = authentication.getName();
        BmiUser bmiUser = userService.findByUsername(username).orElse(null);

        if (bmiUser == null) {
            return "redirect:/login";
        }

        int pageSize = (size != null) ? size : bmiUser.getResultsPerPage();

        BMIFacadeService.BMIStatistics stats = bmiFacadeService.getBMIStatistics(bmiUser.getId());
        Page<WeightRecord> paginatedHistory = bmiFacadeService.getPaginatedUserWeightHistory(bmiUser.getId(), page,
                pageSize);

        List<WeightRecord> currentSlice = paginatedHistory.getContent();
        // Reverse for chart: table is newest-first, chart should be chronological
        // (left-to-right)
        List<WeightRecord> chronologicalSlice = currentSlice.stream()
                .sorted(Comparator.comparing(WeightRecord::getRecordDate))
                .toList();

        List<String> chartLabels = chronologicalSlice.stream()
                .map(r -> r.getRecordDate().toString())
                .collect(Collectors.toList());
        List<Double> chartData = chronologicalSlice.stream()
                .map(WeightRecord::getWeight)
                .collect(Collectors.toList());

        model.addAttribute("user", bmiUser);
        model.addAttribute("stats", stats);
        model.addAttribute("weightHistory", paginatedHistory.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedHistory.getTotalPages());
        model.addAttribute("totalItems", paginatedHistory.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "profile";
    }

    @PostMapping("/update-pagination")
    public String updatePagination(@RequestParam Integer size, Authentication authentication) {
        String username = authentication.getName();
        userService.findByUsername(username).ifPresent(user -> {
            user.setResultsPerPage(size);
            userService.getUserRepository().save(user);
        });
        return "redirect:/profile";
    }

    @PostMapping("/addWeight")
    public String addWeight(@RequestParam Double weight, @RequestParam(required = false) String recordDate,
            Authentication authentication) {

        String username = authentication.getName();
        BmiUser bmiUser = userService.findByUsername(username).orElse(null);

        if (bmiUser != null) {
            try {
                if (recordDate != null && !recordDate.isEmpty()) {
                    bmiFacadeService.recordWeight(bmiUser.getId(), weight, LocalDate.parse(recordDate));
                } else {
                    bmiFacadeService.recordWeight(bmiUser.getId(), weight);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return "redirect:/profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam Double height,
            @RequestParam(defaultValue = "false") Boolean isPublic,
            @RequestParam(required = false) String motivationalQuote,
            @RequestParam(required = false) String achievement,
            Authentication authentication) {

        String username = authentication.getName();
        BmiUser bmiUser = userService.findByUsername(username).orElse(null);

        if (bmiUser != null) {
            bmiFacadeService.updateUserProfile(
                    bmiUser.getId(), firstName, lastName, height, isPublic, motivationalQuote, achievement);
        }

        return "redirect:/profile";
    }

    @PostMapping("/delete-weight/{recordId}")
    public String deleteWeightRecord(@PathVariable Long recordId, Authentication authentication) {

        String username = authentication.getName();
        BmiUser user = userService.findByUsername(username).orElse(null);

        if (user != null) {
            bmiFacadeService.deleteWeightRecord(user.getId(), recordId);
        }

        return "redirect:/profile";
    }
}
