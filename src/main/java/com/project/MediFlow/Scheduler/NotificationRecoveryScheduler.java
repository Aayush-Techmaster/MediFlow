package com.project.MediFlow.Scheduler;

import com.project.MediFlow.Enum.AppointmentEventType;
import com.project.MediFlow.Enum.NotificationStatus;
import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import com.project.MediFlow.RabbitMQ.Producer.AppointmentProducer;
import com.project.MediFlow.Repository.AppointmentRepository;
import com.project.MediFlow.entities.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRecoveryScheduler {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentProducer appointmentProducer;

    @Scheduled(fixedRate = 300000)
    public void recoverPendingNotifications() {

        List<Appointment> pendingAppointments =
                appointmentRepository.findPendingNotificationsWithDetails(
                        NotificationStatus.PENDING
                );

        for (Appointment appointment : pendingAppointments) {

            AppointmentEvent event = new AppointmentEvent(
                    AppointmentEventType.APPOINTMENT_CREATED,
                    appointment.getId(),

                    appointment.getPatient().getFirstName()
                            + " "
                            + appointment.getPatient().getLastName(),

                    appointment.getPatient().getEmail(),

                    appointment.getDoctor().getFirstName()
                            + " "
                            + appointment.getDoctor().getLastName(),

                    appointment.getAppointmentDateTime(),

                    null,
                    null
            );

            appointmentProducer.publishAppointmentEvent(event);

            System.out.println(
                    "Recovery: Republished notification for appointment "
                            + appointment.getId()
            );
        }
    }
}