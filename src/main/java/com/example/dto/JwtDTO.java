package com.example.dto;

import com.example.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtDTO {
    private Long id;
    private String username;
    private List<ProfileRole> roleList;

    public JwtDTO(Long id, String username, List<ProfileRole> roleList, String token, Object o) {
        this.id = id;
        this.username = username;
        this.roleList = roleList;
    }
}
