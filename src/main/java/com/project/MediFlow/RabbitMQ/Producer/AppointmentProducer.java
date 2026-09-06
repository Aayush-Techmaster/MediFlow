package com.project.MediFlow.RabbitMQ.Producer;

import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class AppointmentProducer {
    private final RabbitTemplate rabbitTemplate;

    public AppointmentProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate=rabbitTemplate;
    }


    public void publishAppointmentEvent(AppointmentEvent event){
        rabbitTemplate.convertAndSend(
                "appointment.exchange",
                "appointment.notification",
                event
        );
    }
}
