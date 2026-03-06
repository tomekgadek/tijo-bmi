package com.example.bmimanager.user.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    Long id;
    String username;
    String password; // Added for security config
    String firstName;
    String lastName;
    Double height;
    Boolean isPublic;
    Boolean isBlocked; // Added for security config
    String motivationalQuote;
    String achievement;
    Integer resultsPerPage;
}
