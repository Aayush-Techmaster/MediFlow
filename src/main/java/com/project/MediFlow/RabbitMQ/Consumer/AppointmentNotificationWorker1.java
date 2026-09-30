package com.project.MediFlow.RabbitMQ.Consumer;

import com.project.MediFlow.Email.AppointmentEmailService;
import com.project.MediFlow.Enum.NotificationStatus;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.Repository.AppointmentRepository;
import com.project.MediFlow.entities.Appointment;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppointmentNotificationWorker1 {

    private final AppointmentEmailService appointmentEmailService;
    private final AppointmentRepository appointmentRepository;

    @RabbitListener(queues = "appointment.notification.queue")
    public void consumeAppointmentEvent(
            AppointmentEvent event,
            Channel channel,
            Message message) throws IOException {

        long deliveryTag =
                message.getMessageProperties().getDeliveryTag();

        try {
            // 1. Send the email
            appointmentEmailService.sendAppointmentNotification(event);

            // 2. Email was successfully sent
            // Mark notification as SENT
            Appointment appointment = appointmentRepository
                    .findById(event.getAppointmentId())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Appointment not found with id: "
                                            + event.getAppointmentId()
                            )
                    );

            appointment.setNotificationStatus(
                    NotificationStatus.SENT
            );

            appointmentRepository.save(appointment);

            // 3. ACK only after email + database update succeed
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {

            System.out.println(
                    "WORKER 1: Failed to process message: "
                            + e.getMessage()
            );

            // Reject message -> DLQ
            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );
        }
    }
}