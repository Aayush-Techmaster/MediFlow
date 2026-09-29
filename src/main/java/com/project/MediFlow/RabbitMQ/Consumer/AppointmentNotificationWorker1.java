package com.project.MediFlow.RabbitMQ.Consumer;

import com.project.MediFlow.Email.AppointmentEmailService;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.Email.AppointmentEmailService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class AppointmentNotificationWorker1 {

    private final AppointmentEmailService appointmentEmailService;

    public AppointmentNotificationWorker1(
            AppointmentEmailService appointmentEmailService) {

        this.appointmentEmailService = appointmentEmailService;
    }

    @RabbitListener(queues = "appointment.notification.queue")
    public void consumeAppointmentEvent(AppointmentEvent event,
                                        Channel channel,
                                        Message message)throws IOException {


        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println(
                    "WORKER 1 Received appointment event: " + event.getEventType()
            );

            appointmentEmailService.sendAppointmentNotification(event);

            // Email sent successfully → ACK
            channel.basicAck(deliveryTag, false);

            System.out.println(
                    "WORKER 1: Message acknowledged"
            );
        }catch(Exception e){
                System.out.println(
                        "WORKER 1: Failed to process message: "
                                + e.getMessage()
                );

                channel.basicNack(deliveryTag, false, false);

            }
        }




    }
