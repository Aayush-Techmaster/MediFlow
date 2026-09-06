package com.project.MediFlow.RabbitMQ.Consumer;

import com.project.MediFlow.Email.EmailService;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.RabbitMQ.Event.PatientRegisteredEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {


    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "patient.registered.queue")
    public void consumePatientRegisteredEvent(PatientRegisteredEvent event) {

        emailService.sendWelcomeEmail(
                event.getEmail(),
                event.getPatientName()
        );
    }
}