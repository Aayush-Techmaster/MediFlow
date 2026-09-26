package com.project.MediFlow.Service.Impl;

import com.project.MediFlow.Dtos.RegisterRequest;
import com.project.MediFlow.Dtos.RegisterRequest;
import com.project.MediFlow.Enum.Role;
import com.project.MediFlow.entities.User;
import com.project.MediFlow.Repository.UserRepository;
import com.project.MediFlow.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "User with this email already exists"
            );
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .enabled(true)
                .build();

        userRepository.save(user);

    }
}