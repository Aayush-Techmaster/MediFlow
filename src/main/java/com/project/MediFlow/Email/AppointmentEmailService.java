package com.project.MediFlow.Email;

import com.project.MediFlow.RabbitMQ.Event.AppointmentEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEmailService {

    private final JavaMailSender mailSender;

    public AppointmentEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentNotification(AppointmentEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.getPatientEmail());

        switch (event.getEventType()) {

            case APPOINTMENT_CREATED:

                message.setSubject("Appointment Created - MediFlow");

                message.setText(
                        "Hello " + event.getPatientName() + ",\n\n" +
                                "Your appointment has been successfully created.\n\n" +
                                "Doctor: " + event.getDoctorName() + "\n" +
                                "Appointment Time: " + event.getAppointmentDateTime() + "\n\n" +
                                "Thank you,\n" +
                                "MediFlow"
                );

                break;


            case APPOINTMENT_CONFIRMED:

                message.setSubject("Appointment Confirmed - MediFlow");

                message.setText(
                        "Hello " + event.getPatientName() + ",\n\n" +
                                "Your appointment has been confirmed.\n\n" +
                                "Doctor: " + event.getDoctorName() + "\n" +
                                "Appointment Time: " + event.getAppointmentDateTime() + "\n\n" +
                                "Thank you,\n" +
                                "MediFlow"
                );

                break;


            case APPOINTMENT_RESCHEDULED:

                message.setSubject("Appointment Rescheduled - MediFlow");

                message.setText(
                        "Hello " + event.getPatientName() + ",\n\n" +
                                "Your appointment has been rescheduled.\n\n" +
                                "Doctor: " + event.getDoctorName() + "\n" +
                                "Previous Time: " + event.getOldAppointmentDateTime() + "\n" +
                                "New Time: " + event.getNewAppointmentDateTime() + "\n\n" +
                                "Thank you,\n" +
                                "MediFlow"
                );

                break;


            case APPOINTMENT_CANCELLED:

                message.setSubject("Appointment Cancelled - MediFlow");

                message.setText(
                        "Hello " + event.getPatientName() + ",\n\n" +
                                "Your appointment has been cancelled.\n\n" +
                                "Doctor: " + event.getDoctorName() + "\n" +
                                "Appointment Time: " + event.getAppointmentDateTime() + "\n\n" +
                                "Thank you,\n" +
                                "MediFlow"
                );

                break;


            case APPOINTMENT_COMPLETED:

                message.setSubject("Appointment Completed - MediFlow");

                message.setText(
                        "Hello " + event.getPatientName() + ",\n\n" +
                                "Your appointment has been completed.\n\n" +
                                "Doctor: " + event.getDoctorName() + "\n" +
                                "Appointment Time: " + event.getAppointmentDateTime() + "\n\n" +
                                "Thank you for using MediFlow."
                );

                break;
        }

        mailSender.send(message);

        System.out.println(
                "Appointment email sent to: "
                        + event.getPatientEmail()
        );
    }
}