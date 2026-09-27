package com.project.MediFlow.security;

import com.project.MediFlow.Repository.AppointmentRepository;
import com.project.MediFlow.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("patientAuthorization")
@RequiredArgsConstructor
public class PatientAuthorizationService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public boolean isOwnPatient(Long patientId, String email) {
        return patientRepository.findById(patientId)
                .map(patient -> patient.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }

    public boolean isOwnAppointment(Long appointmentId, String email) {
        return appointmentRepository.existsByIdAndPatient_Email(
                appointmentId,
                email
        );
    }

    public boolean canCreatePatient(String patientEmail, String authenticatedEmail) {
        return patientEmail != null
                && authenticatedEmail != null
                && patientEmail.equalsIgnoreCase(authenticatedEmail);
    }

    public boolean canCreateAppointment(Long patientId, String email) {
        return isOwnPatient(patientId, email);
    }
}
