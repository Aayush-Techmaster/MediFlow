package com.project.MediFlow.Service.Impl;

import com.project.MediFlow.Dtos.PatientRequest;
import com.project.MediFlow.Dtos.PatientResponse;
import com.project.MediFlow.Exception.DuplicateResourceException;
import com.project.MediFlow.Exception.PatientNotFoundException;
import com.project.MediFlow.Repository.PatientRepository;
import com.project.MediFlow.Service.PatientService;
import com.project.MediFlow.entities.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<PatientResponse> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(patient -> PatientResponse.builder()
                        .id(patient.getId())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .age(patient.getAge())
                        .gender(patient.getGender())
                        .phone(patient.getPhone())
                        .email(patient.getEmail())
                        .address(patient.getAddress())
                        .build()
                )
                .toList();
    }

    @Override
    public Optional<PatientResponse> getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id: " + id
                        )
                );

        return Optional.ofNullable(PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .build());
    }


    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id: " + id
                        )
                );

        if (patientRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException(
                    "Email already exists."
            );
        }

        if (patientRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new DuplicateResourceException(
                    "Phone number already exists."
            );
        }

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        Patient updatedPatient = patientRepository.save(patient);

        return PatientResponse.builder()
                .id(updatedPatient.getId())
                .firstName(updatedPatient.getFirstName())
                .lastName(updatedPatient.getLastName())
                .age(updatedPatient.getAge())
                .gender(updatedPatient.getGender())
                .phone(updatedPatient.getPhone())
                .email(updatedPatient.getEmail())
                .address(updatedPatient.getAddress())
                .build();
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id: " + id
                        )
                );

        patientRepository.delete(patient);
    }
}