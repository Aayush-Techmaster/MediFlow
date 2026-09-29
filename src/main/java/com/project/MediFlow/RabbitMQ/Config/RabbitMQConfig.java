package com.project.MediFlow.RabbitMQ.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.QueueBuilder;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "patient.registered.queue";
    public static final String EXCHANGE_NAME = "patient.exchange";
    public static final String ROUTING_KEY = "patient.registered";

    public static final String APPOINTMENT_QUEUE_NAME =
            "appointment.notification.queue";

    public static final String APPOINTMENT_EXCHANGE_NAME =
            "appointment.exchange";

    public static final String APPOINTMENT_ROUTING_KEY =
            "appointment.notification";

    public static final String APPOINTMENT_DLX =
            "appointment.dlx";

    public static final String APPOINTMENT_DLQ =
            "appointment.notification.dlq";

    public static final String APPOINTMENT_DLQ_ROUTING_KEY =
            "appointment.notification.dlq";

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
    @Bean
    public Queue patientQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public DirectExchange patientExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding patientBinding(
            Queue patientQueue,
            DirectExchange patientExchange) {

        return BindingBuilder
                .bind(patientQueue)
                .to(patientExchange)
                .with(ROUTING_KEY);
    }


    @Bean
    public Queue appointmentNotificationQueue() {

        return QueueBuilder
                .durable(APPOINTMENT_QUEUE_NAME)
                .deadLetterExchange(APPOINTMENT_DLX)
                .deadLetterRoutingKey(APPOINTMENT_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange appointmentExchange() {
        return new DirectExchange(APPOINTMENT_EXCHANGE_NAME);
    }

    @Bean
    public Binding appointmentNotificationBinding(
            Queue appointmentNotificationQueue,
            DirectExchange appointmentExchange) {

        return BindingBuilder
                .bind(appointmentNotificationQueue)
                .to(appointmentExchange)
                .with(APPOINTMENT_ROUTING_KEY);
    }

    @Bean
    public DirectExchange appointmentDeadLetterExchange() {
        return new DirectExchange(APPOINTMENT_DLX);
    }

    @Bean
    public Queue appointmentDeadLetterQueue() {
        return QueueBuilder
                .durable(APPOINTMENT_DLQ)
                .build();
    }

    @Bean
    public Binding appointmentDeadLetterBinding(
            Queue appointmentDeadLetterQueue,
            DirectExchange appointmentDeadLetterExchange) {

        return BindingBuilder
                .bind(appointmentDeadLetterQueue)
                .to(appointmentDeadLetterExchange)
                .with(APPOINTMENT_DLQ_ROUTING_KEY);
    }
}