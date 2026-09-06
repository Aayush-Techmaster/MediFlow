package com.project.MediFlow.RabbitMQ.Event;


import com.project.MediFlow.Enum.AppointmentEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEvent {

    private AppointmentEventType eventType;

    private Long appointmentId;

    private String patientName;
    private String patientEmail;

    private String doctorName;

    private LocalDateTime appointmentDateTime;

    private LocalDateTime oldAppointmentDateTime;
    private LocalDateTime newAppointmentDateTime;
}
