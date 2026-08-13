package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.DoctorRequest;
import com.project.MediFlow.Dtos.DoctorResponse;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    void deleteDoctor(Long id);
}
