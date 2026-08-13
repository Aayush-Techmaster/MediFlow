package com.project.MediFlow.Dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private int age;

    private String gender;

    private String specialization ;

    private String phone;

    private String email;
}
