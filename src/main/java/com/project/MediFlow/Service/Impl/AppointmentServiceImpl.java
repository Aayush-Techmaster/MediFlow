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
import jakarta.transaction.Transactional;
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

    @Transactional
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


    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id
                        )
                );

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
    }

    @Override
    public AppointmentResponse cancelAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id
                        )
                );

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Appointment is already cancelled"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed appointment cannot be cancelled"
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        Patient patient = patientRepository
                .findById(savedAppointment.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + savedAppointment.getPatientId()
                        )
                );

        Doctor doctor = doctorRepository
                .findById(savedAppointment.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + savedAppointment.getDoctorId()
                        )
                );

        return mapToResponse(
                savedAppointment,
                patient,
                doctor
        );
    }

    @Override
    public AppointmentResponse confirmAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id
                        )
                );

        if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Appointment is already confirmed"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled appointment cannot be confirmed"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed appointment cannot be confirmed"
            );
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        Patient patient = patientRepository
                .findById(savedAppointment.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + savedAppointment.getPatientId()
                        )
                );

        Doctor doctor = doctorRepository
                .findById(savedAppointment.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + savedAppointment.getDoctorId()
                        )
                );

        return mapToResponse(
                savedAppointment,
                patient,
                doctor
        );
    }

    @Override
    public AppointmentResponse completeAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id
                        )
                );

        if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException(
                    "Scheduled appointment cannot be completed"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled appointment cannot be completed"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Appointment is already completed"
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        Patient patient = patientRepository
                .findById(savedAppointment.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + savedAppointment.getPatientId()
                        )
                );

        Doctor doctor = doctorRepository
                .findById(savedAppointment.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + savedAppointment.getDoctorId()
                        )
                );

        return mapToResponse(
                savedAppointment,
                patient,
                doctor
        );
    }

    @Override
    public AppointmentResponse rescheduleAppointment(
            Long id,
            LocalDateTime newAppointmentDateTime) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id
                        )
                );

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled appointment cannot be rescheduled"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed appointment cannot be rescheduled"
            );
        }

        if (newAppointmentDateTime == null) {
            throw new IllegalArgumentException(
                    "New appointment date and time cannot be null"
            );
        }

        if (!newAppointmentDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "New appointment date and time must be in the future"
            );
        }

        boolean doctorAlreadyBooked =
                appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndIdNot(
                        appointment.getDoctorId(),
                        newAppointmentDateTime,
                        id
                );

        if (doctorAlreadyBooked) {
            throw new IllegalStateException(
                    "Doctor is already booked for this time"
            );
        }

        appointment.setAppointmentDateTime(newAppointmentDateTime);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        Patient patient = patientRepository
                .findById(savedAppointment.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + savedAppointment.getPatientId()
                        )
                );

        Doctor doctor = doctorRepository
                .findById(savedAppointment.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + savedAppointment.getDoctorId()
                        )
                );

        return mapToResponse(
                savedAppointment,
                patient,
                doctor
        );
    }



}