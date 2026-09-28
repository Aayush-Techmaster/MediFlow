package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.AdminUserResponse;
import com.project.MediFlow.Dtos.UserRoleResponse;
import com.project.MediFlow.Enum.Role;

import java.util.List;

public interface AdminUserService {

    List<AdminUserResponse> getAllUsers();

    UserRoleResponse updateUserRole(Long userId, Role role);
}
