package com.project.MediFlow.Service.Impl;

import com.project.MediFlow.Dtos.UserRoleResponse;
import com.project.MediFlow.Enum.Role;
import com.project.MediFlow.Exception.ResourceNotFoundException;
import com.project.MediFlow.Repository.UserRepository;
import com.project.MediFlow.Service.AdminUserService;
import com.project.MediFlow.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private static final String SYSTEM_ADMIN_EMAIL = "admin@mediflow.com";

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserRoleResponse updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }

        if (SYSTEM_ADMIN_EMAIL.equalsIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException(
                    "System admin role cannot be changed");
        }

        if (role == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "ADMIN role cannot be assigned through this endpoint");
        }

        user.setRole(role);
        User savedUser = userRepository.save(user);

        return new UserRoleResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.isEnabled()
        );
    }
}
