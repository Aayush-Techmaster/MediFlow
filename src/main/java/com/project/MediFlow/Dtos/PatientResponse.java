package com.project.MediFlow.Dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private int age;

    private String gender;

    private String phone;

    private String email;

    private String address;
}