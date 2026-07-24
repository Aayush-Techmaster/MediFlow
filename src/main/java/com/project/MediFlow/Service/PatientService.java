package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.PatientRequest;
import com.project.MediFlow.Dtos.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



public interface PatientService {
    PatientResponse createPatient(PatientRequest request);
}
