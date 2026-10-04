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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppointmentNotificationWorker2 {

    private final AppointmentEmailService appointmentEmailService;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    @RabbitListener(queues = "appointment.notification.queue")
    public void consumeAppointmentEvent(
            AppointmentEvent event,
            Channel channel,
            Message message) throws IOException {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            Appointment appointment = appointmentRepository
                    .findById(event.getAppointmentId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Appointment not found with id: " + event.getAppointmentId()));

            if (appointment.getNotificationStatus() == NotificationStatus.SENT
                    || appointment.getNotificationStatus() == NotificationStatus.PROCESSING) {
                channel.basicAck(deliveryTag, false);
                return;
            }

            appointment.setNotificationStatus(NotificationStatus.PROCESSING);
            appointmentRepository.saveAndFlush(appointment);

            appointmentEmailService.sendAppointmentNotification(event);

            appointment.setNotificationStatus(NotificationStatus.SENT);
            appointmentRepository.save(appointment);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            try {
                Appointment appointment = appointmentRepository
                        .findById(event.getAppointmentId())
                        .orElse(null);
                if (appointment != null) {
                    appointment.setNotificationStatus(NotificationStatus.FAILED);
                    appointmentRepository.save(appointment);
                }
            } catch (Exception statusException) {
                System.out.println("WORKER 2: Failed to update notification status: "
                        + statusException.getMessage());
            }

            System.out.println("WORKER 2: Failed to process message: " + e.getMessage());
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
