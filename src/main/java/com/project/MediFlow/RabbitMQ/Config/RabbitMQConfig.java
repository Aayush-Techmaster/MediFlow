package com.project.MediFlow.RabbitMQ.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "patient.registered.queue";
    public static final String EXCHANGE_NAME = "patient.exchange";
    public static final String ROUTING_KEY = "patient.registered";

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
}