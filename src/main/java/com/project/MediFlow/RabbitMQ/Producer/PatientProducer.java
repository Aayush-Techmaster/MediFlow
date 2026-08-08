package com.project.MediFlow.RabbitMQ.Producer;

import com.project.MediFlow.RabbitMQ.Event.PatientRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PatientProducer {

    private final RabbitTemplate rabbitTemplate;

    public PatientProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPatientRegisteredEvent(PatientRegisteredEvent event) {

        rabbitTemplate.convertAndSend(
                "patient.exchange",
                "patient.registered",
                event
        );

    }
}