package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.UserRoleResponse;
import com.project.MediFlow.Enum.Role;

public interface AdminUserService {

    UserRoleResponse updateUserRole(Long userId, Role role);
}
