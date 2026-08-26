package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.PatientRequest;
import com.project.MediFlow.Dtos.PatientResponse;
import com.project.MediFlow.entities.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface PatientService {
    PatientResponse createPatient(PatientRequest request);

    List<PatientResponse> getAllPatients();

    Optional<PatientResponse> getPatientById(Long id);

    PatientResponse updatePatient(Long id, PatientRequest request);

    void deletePatient(Long id);
}


