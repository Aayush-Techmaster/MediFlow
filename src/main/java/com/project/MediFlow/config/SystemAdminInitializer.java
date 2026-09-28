package com.project.MediFlow.config;

import com.project.MediFlow.Enum.Role;
import com.project.MediFlow.Repository.UserRepository;
import com.project.MediFlow.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${system.admin.email:admin@mediflow.com}")
    private String adminEmail;

    @Value("${system.admin.password:Admin@123}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseGet(() -> User.builder()
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build());

        if (admin.getRole() != Role.ADMIN) {
            admin.setRole(Role.ADMIN);
        }

        if (!admin.isEnabled()) {
            admin.setEnabled(true);
        }

        userRepository.save(admin);
    }
}
