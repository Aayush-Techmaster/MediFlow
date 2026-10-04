package com.project.MediFlow.Service;

import com.project.MediFlow.Enum.NotificationStatus;
import com.project.MediFlow.Repository.AppointmentRepository;
import com.project.MediFlow.entities.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationStateService {

    private final AppointmentRepository appointmentRepository;

    /**
     * Atomically claims an appointment notification before any external side effect.
     * The transaction commits before the email is sent.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean claimNotification(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdForNotificationUpdate(appointmentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Appointment not found with id: " + appointmentId));

        NotificationStatus status = appointment.getNotificationStatus();

        if (status == NotificationStatus.SENT || status == NotificationStatus.PROCESSING) {
            return false;
        }

        appointment.setNotificationStatus(NotificationStatus.PROCESSING);
        appointmentRepository.save(appointment);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSent(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Appointment not found with id: " + appointmentId));

        appointment.setNotificationStatus(NotificationStatus.SENT);
        appointmentRepository.save(appointment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Appointment not found with id: " + appointmentId));

        appointment.setNotificationStatus(NotificationStatus.FAILED);
        appointmentRepository.save(appointment);
    }
}
