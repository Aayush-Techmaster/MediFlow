package com.project.MediFlow.Dtos;

import com.project.MediFlow.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRoleResponse {

    private Long id;
    private String email;
    private Role assignedRole;
    private boolean enabled;
}
