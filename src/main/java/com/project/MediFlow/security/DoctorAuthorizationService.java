package com.project.MediFlow.security;

import com.project.MediFlow.Repository.AppointmentRepository;
import com.project.MediFlow.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("doctorAuthorization")
@RequiredArgsConstructor
public class DoctorAuthorizationService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public boolean isOwnDoctor(Long doctorId, String email) {
        return doctorRepository.findById(doctorId)
                .map(doctor -> doctor.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }

    public boolean isOwnAppointment(Long appointmentId, String email) {
        return appointmentRepository.existsByIdAndDoctor_Email(
                appointmentId,
                email
        );
    }
}
