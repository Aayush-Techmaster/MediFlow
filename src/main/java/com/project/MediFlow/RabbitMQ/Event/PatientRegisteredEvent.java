package com.project.MediFlow.RabbitMQ.Event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRegisteredEvent {

    private Long patientId;
    private String patientName;
    private String email;
}
