package com.project.MediFlow.entities;

import com.project.MediFlow.Enum.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_doctor_appointment_time",
                        columnNames = {
                                "doctor_id",
                                "appointment_date_time"
                        }
                )
        }
)
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