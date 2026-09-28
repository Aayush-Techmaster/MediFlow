package com.project.MediFlow.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStatusResponse {

    private Long id;
    private String email;
    private boolean enabled;
}