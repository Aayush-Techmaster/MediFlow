package com.project.MediFlow.Dtos;

import com.project.MediFlow.Enum.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    private Long id;

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private String doctorName;

    private LocalDateTime appointmentDateTime;

    private LocalDateTime bookedAt;

    private String reason;

    private AppointmentStatus status;
}