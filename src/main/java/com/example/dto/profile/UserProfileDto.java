package com.example.dto.profile;

import com.example.enums.GeneralStatus;
import com.example.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {
    private Long id;
    private String fullName;
    private String phone;
    private String profileImageUrl;
    private ProfileRole role;
    private GeneralStatus status;
    private Boolean visible;
    private BigDecimal totalBalance;
    private Boolean notificationsEnabled;
    private Boolean darkThemeEnabled;
    private String appLanguageCode;
    private String cityName;
}
