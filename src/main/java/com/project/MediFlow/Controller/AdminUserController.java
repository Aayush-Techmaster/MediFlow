package com.project.MediFlow.Controller;

import com.project.MediFlow.Dtos.UpdateUserRoleRequest;
import com.project.MediFlow.Dtos.UserRoleResponse;
import com.project.MediFlow.Service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserRoleResponse> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequest request) {

        return ResponseEntity.ok(
                adminUserService.updateUserRole(
                        userId,
                        request.getRole()
                )
        );
    }
}
