package com.example.bmimanager.profile;

import com.example.bmimanager.profile.dto.PublicProfileDto;
import com.example.bmimanager.user.domain.UserDto;
import com.example.bmimanager.weight.domain.WeightRecordDto;
import com.example.bmimanager.bmi.domain.BmiFacade;
import com.example.bmimanager.user.domain.UserFacade;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/public")
public class PublicProfileController {

    private final UserFacade userFacade;
    private final BmiFacade bmiFacade;

    public PublicProfileController(UserFacade userFacade, BmiFacade bmiFacade) {
        this.userFacade = userFacade;
        this.bmiFacade = bmiFacade;
    }

    @GetMapping("/profiles")
    public String viewPublicProfiles(Model model) {
        List<UserDto> publicBmiUsers = bmiFacade.getPublicProfiles();
        List<PublicProfileDto> profiles = publicBmiUsers.stream()
                .map(user -> new PublicProfileDto(
                        user,
                        bmiFacade.getUserCurrentWeight(user.getId()),
                        bmiFacade.getUserCurrentBMI(user.getId())))
                .collect(Collectors.toList());

        model.addAttribute("profiles", profiles);
        return "public-profiles";
    }

    @GetMapping("/profile/{userId}")
    public String viewUserProfile(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "25") int size,
            Model model) {
        UserDto bmiUser = userFacade.getUserById(userId);

        if (bmiUser == null || !bmiUser.getIsPublic()) {
            return "redirect:/public/profiles";
        }

        if (page == null) {
            Page<WeightRecordDto> firstPage = bmiFacade.getPaginatedUserWeightHistory(userId, 0, size);
            int lastPage = Math.max(0, firstPage.getTotalPages() - 1);
            return "redirect:/public/profile/" + userId + "?page=" + lastPage;
        }

        Page<WeightRecordDto> paginatedHistory = bmiFacade.getPaginatedUserWeightHistory(userId, page, size);
        BmiFacade.BMIStatistics stats = bmiFacade.getBMIStatistics(userId);

        List<WeightRecordDto> currentSlice = paginatedHistory.getContent();
        List<String> chartLabels = currentSlice.stream()
                .map(r -> r.getRecordDate().toString())
                .collect(Collectors.toList());
        List<Double> chartData = currentSlice.stream()
                .map(WeightRecordDto::getWeight)
                .collect(Collectors.toList());
        List<Long> chartRecordIds = currentSlice.stream()
                .map(WeightRecordDto::getId)
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
        model.addAttribute("chartRecordIds", chartRecordIds);

        return "public-profile-view";
    }
}
