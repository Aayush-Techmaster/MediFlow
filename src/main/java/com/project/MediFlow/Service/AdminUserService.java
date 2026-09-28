package com.project.MediFlow.Service;

import com.project.MediFlow.Enum.Role;

public interface AdminUserService {

    Object updateUserRole(Long userId, Role role);
}
