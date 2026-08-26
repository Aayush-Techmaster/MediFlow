package com.project.MediFlow.Repository;

import com.project.MediFlow.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentDateTime(
            Long doctorId,
            LocalDateTime appointmentDateTime
    );

    boolean existsByDoctorIdAndAppointmentDateTimeAndIdNot(
            Long doctorId,
            LocalDateTime appointmentDateTime,
            Long id
    );
}



