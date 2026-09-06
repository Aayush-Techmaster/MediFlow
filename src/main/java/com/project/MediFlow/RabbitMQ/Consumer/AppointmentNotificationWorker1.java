package com.project.MediFlow.RabbitMQ.Consumer;

import com.project.MediFlow.Email.AppointmentEmailService;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentNotificationWorker1 {

    private final AppointmentEmailService appointmentEmailService;

    public AppointmentNotificationWorker1(
            AppointmentEmailService appointmentEmailService) {

        this.appointmentEmailService = appointmentEmailService;
    }

    @RabbitListener(queues = "appointment.notification.queue")
    public void consumeAppointmentEvent(AppointmentEvent event) {

        System.out.println(
                "WORKER 1 received appointment event: "
                        + event.getEventType()
        );

        appointmentEmailService.sendAppointmentNotification(event);
    }
}