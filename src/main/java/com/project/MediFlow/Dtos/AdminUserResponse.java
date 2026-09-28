package com.project.MediFlow.Dtos;

import com.project.MediFlow.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminUserResponse {

    private Long id;
    private String email;
    private Role role;
    private boolean enabled;
}
