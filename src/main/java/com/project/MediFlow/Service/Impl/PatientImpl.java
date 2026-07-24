package com.project.MediFlow.Service.Impl;

import com.project.MediFlow.Dtos.PatientRequest;
import com.project.MediFlow.Dtos.PatientResponse;
import com.project.MediFlow.Exception.DuplicateResourceException;
import com.project.MediFlow.Repository.PatientRepository;
import com.project.MediFlow.Service.PatientService;
import com.project.MediFlow.entities.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Override
    public PatientResponse createPatient(PatientRequest request) {

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return PatientResponse.builder()
                .id(savedPatient.getId())
                .firstName(savedPatient.getFirstName())
                .lastName(savedPatient.getLastName())
                .age(savedPatient.getAge())
                .gender(savedPatient.getGender())
                .phone(savedPatient.getPhone())
                .email(savedPatient.getEmail())
                .address(savedPatient.getAddress())
                .build();
    }
}