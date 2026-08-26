package com.project.MediFlow.Service.Impl;

import com.project.MediFlow.Dtos.AppointmentRequest;
import com.project.MediFlow.Dtos.AppointmentResponse;
import com.project.MediFlow.Enum.AppointmentStatus;
import com.project.MediFlow.Exception.ResourceNotFoundException;
import com.project.MediFlow.Exception.DuplicateResourceException;
import com.project.MediFlow.Repository.AppointmentRepository;
import com.project.MediFlow.Repository.DoctorRepository;
import com.project.MediFlow.Repository.PatientRepository;
import com.project.MediFlow.Service.AppointmentService;
import com.project.MediFlow.entities.Appointment;
import com.project.MediFlow.entities.Doctor;
import com.project.MediFlow.entities.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentResponse createAppointment(
            AppointmentRequest request) {

        // 1. Check patient
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + request.getPatientId()
                        )
                );

        // 2. Check doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + request.getDoctorId()
                        )
                );

        // 3. Check doctor's availability
        if (appointmentRepository
                .existsByDoctorIdAndAppointmentDateTime(
                        request.getDoctorId(),
                        request.getAppointmentDateTime())) {

            throw new DuplicateResourceException(
                    "Doctor is already booked for this time."
            );
        }

        // 4. Create appointment
        Appointment appointment = Appointment.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .appointmentDateTime(
                        request.getAppointmentDateTime()
                )
                .bookedAt(LocalDateTime.now())
                .reason(request.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        // 5. Return response
        return mapToResponse(savedAppointment, patient, doctor);
    }

    private AppointmentResponse mapToResponse(
            Appointment appointment,
            Patient patient,
            Doctor doctor) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(patient.getId())
                .patientName(
                        patient.getFirstName()
                                + " "
                                + patient.getLastName()
                )
                .doctorId(doctor.getId())
                .doctorName(
                        doctor.getFirstName()
                                + " "
                                + doctor.getLastName()
                )
                .appointmentDateTime(
                        appointment.getAppointmentDateTime()
                )
                .bookedAt(appointment.getBookedAt())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .build();
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(appointment -> {

                    Patient patient = patientRepository
                            .findById(appointment.getPatientId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Patient not found with id: "
                                                    + appointment.getPatientId()
                                    )
                            );

                    Doctor doctor = doctorRepository
                            .findById(appointment.getDoctorId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Doctor not found with id: "
                                                    + appointment.getDoctorId()
                                    )
                            );

                    return mapToResponse(
                            appointment,
                            patient,
                            doctor
                    );
                })
                .toList();
    }
}