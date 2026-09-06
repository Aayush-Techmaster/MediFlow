package com.project.MediFlow.Controller;

import com.project.MediFlow.Enum.AppointmentEventType;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.RabbitMQ.Producer.AppointmentProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class RabbitMQTestController {

    private final AppointmentProducer appointmentProducer;

    public RabbitMQTestController(
            AppointmentProducer appointmentProducer) {
        this.appointmentProducer = appointmentProducer;
    }

    @PostMapping("/appointment-events")
    public String sendTestAppointmentEvents() {

        for (int i = 1; i <= 10; i++) {

            AppointmentEvent event = new AppointmentEvent(
                    AppointmentEventType.APPOINTMENT_CREATED,
                    (long) i,
                    "Test Patient " + i,
                    "aayush.pawar54321@gmail.com",
                    "Dr. Test",
                    LocalDateTime.now().plusDays(i),
                    null,
                    null
            );

            appointmentProducer.publishAppointmentEvent(event);
        }

        return "10 appointment events sent to RabbitMQ";
    }
}