package com.example.bmimanager.profile;

import com.example.bmimanager.user.domain.BmiUser;
import com.example.bmimanager.weight.domain.WeightRecord;
import com.example.bmimanager.bmi.domain.BmiFacade;
import com.example.bmimanager.user.domain.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private final BmiFacade bmiFacade;
    private final UserService userService;

    public UserProfileController(BmiFacade bmiFacade, UserService userService) {
        this.bmiFacade = bmiFacade;
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            Authentication authentication, Model model) {
        String username = authentication.getName();
        BmiUser bmiUser = userService.findByUsername(username).orElse(null);

        if (bmiUser == null) {
            return "redirect:/login";
        }

        int pageSize = (size != null) ? size : bmiUser.getResultsPerPage();

        if (page == null) {
            Page<WeightRecord> firstPage = bmiFacade.getPaginatedUserWeightHistory(bmiUser.getId(), 0, pageSize);
            int lastPage = Math.max(0, firstPage.getTotalPages() - 1);
            return "redirect:/profile?page=" + lastPage;
        }

        BmiFacade.BMIStatistics stats = bmiFacade.getBMIStatistics(bmiUser.getId());
        Page<WeightRecord> paginatedHistory = bmiFacade.getPaginatedUserWeightHistory(bmiUser.getId(), page, pageSize);

        List<WeightRecord> chronologicalSlice = paginatedHistory.getContent();

        List<String> chartLabels = chronologicalSlice.stream()
                .map(r -> r.getRecordDate().toString())
                .collect(Collectors.toList());
        List<Double> chartData = chronologicalSlice.stream()
                .map(WeightRecord::getWeight)
                .collect(Collectors.toList());
        List<Long> chartRecordIds = chronologicalSlice.stream()
                .map(WeightRecord::getId)
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
        model.addAttribute("chartRecordIds", chartRecordIds);

        return "profile";
    }

    @PostMapping("/update-pagination")
    public String updatePagination(@RequestParam Integer size, Authentication authentication) {
        String username = authentication.getName();
        userService.findByUsername(username).ifPresent(user -> {
            user.setResultsPerPage(size);
            userService.saveUser(user);
        });
        return "redirect:/profile";
    }

    @PostMapping("/addWeight")
    public String addWeight(@RequestParam Double weight, @RequestParam(required = false) String recordDate,
            @RequestParam(required = false) Integer page,
            Authentication authentication) {

        String username = authentication.getName();
        BmiUser bmiUser = userService.findByUsername(username).orElse(null);

        if (bmiUser != null) {
            try {
                if (recordDate != null && !recordDate.isEmpty()) {
                    bmiFacade.recordWeight(bmiUser.getId(), weight, LocalDate.parse(recordDate));
                } else {
                    bmiFacade.recordWeight(bmiUser.getId(), weight);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return page != null ? "redirect:/profile?page=" + page : "redirect:/profile";
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
            bmiFacade.updateUserProfile(
                    bmiUser.getId(), firstName, lastName, height, isPublic, motivationalQuote, achievement);
        }

        return "redirect:/profile";
    }

    @PostMapping("/edit-weight/{recordId}")
    public String editWeightRecord(@PathVariable Long recordId, @RequestParam Double weight,
            @RequestParam(required = false) Integer page, Authentication authentication) {

        String username = authentication.getName();
        BmiUser user = userService.findByUsername(username).orElse(null);

        if (user != null) {
            bmiFacade.updateWeightRecord(user.getId(), recordId, weight);
        }

        return page != null ? "redirect:/profile?page=" + page : "redirect:/profile";
    }

    @PostMapping("/delete-weight/{recordId}")
    public String deleteWeightRecord(@PathVariable Long recordId, @RequestParam(required = false) Integer page,
            Authentication authentication) {

        String username = authentication.getName();
        BmiUser user = userService.findByUsername(username).orElse(null);

        if (user != null) {
            bmiFacade.deleteWeightRecord(user.getId(), recordId);
        }

        return page != null ? "redirect:/profile?page=" + page : "redirect:/profile";
    }
}
