package com.project.MediFlow.Repository;

import com.project.MediFlow.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctor_IdAndAppointmentDateTime(
            Long doctorId,
            LocalDateTime appointmentDateTime
    );

    boolean existsByDoctor_IdAndAppointmentDateTimeAndIdNot(
            Long doctorId,
            LocalDateTime appointmentDateTime,
            Long id
    );

    @Query("""
        SELECT a
        FROM Appointment a
        JOIN FETCH a.patient
        JOIN FETCH a.doctor
        """)
    List<Appointment> findAllWithPatientAndDoctor();



    @Query("""
        SELECT a
        FROM Appointment a
        JOIN FETCH a.patient
        JOIN FETCH a.doctor
        WHERE a.id = :id
        """)
    Optional<Appointment> findByIdWithPatientAndDoctor(
            @Param("id") Long id
    );
}



