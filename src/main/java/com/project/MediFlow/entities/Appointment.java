package com.project.MediFlow.entities;

import com.project.MediFlow.Enum.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    private Long doctorId;

    private LocalDateTime appointmentDateTime;

    private LocalDateTime bookedAt;

    private String reason;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}