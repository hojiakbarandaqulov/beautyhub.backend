package com.example.dto;

import com.example.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtDTO {
    private Integer id;
    private String username;
    private List<ProfileRole> roleList;

    public JwtDTO(Integer id) {
        this.id = id;
    }
}
