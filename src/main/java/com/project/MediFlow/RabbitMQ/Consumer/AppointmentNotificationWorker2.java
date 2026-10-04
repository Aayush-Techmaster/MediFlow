package com.project.MediFlow.RabbitMQ.Consumer;

import com.project.MediFlow.Email.AppointmentEmailService;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.Service.NotificationStateService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppointmentNotificationWorker2 {

    private final AppointmentEmailService appointmentEmailService;
    private final NotificationStateService notificationStateService;

    @RabbitListener(queues = "appointment.notification.queue")
    public void consumeAppointmentEvent(
            AppointmentEvent event,
            Channel channel,
            Message message) throws IOException {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Long appointmentId = event.getAppointmentId();

        try {
            // Atomically claim the notification in its own committed transaction.
            boolean claimed = notificationStateService.claimNotification(appointmentId);

            if (!claimed) {
                // Already SENT or currently PROCESSING. Nothing more to do.
                channel.basicAck(deliveryTag, false);
                return;
            }

            // External side effect happens after PROCESSING is committed.
            appointmentEmailService.sendAppointmentNotification(event);

            // Persist SENT in a separate transaction.
            notificationStateService.markSent(appointmentId);

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            try {
                // Persist FAILED even though the RabbitMQ message will be rejected.
                notificationStateService.markFailed(appointmentId);
            } catch (Exception statusException) {
                System.out.println(
                        "WORKER 2: Failed to persist FAILED status: "
                                + statusException.getMessage()
                );
            }

            System.out.println(
                    "WORKER 2: Failed to process message: "
                            + e.getMessage()
            );

            // Preserve the existing DLQ flow.
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
