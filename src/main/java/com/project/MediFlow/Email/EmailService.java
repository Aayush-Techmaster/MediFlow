package com.project.MediFlow.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(
            String to,
            String patientName) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Welcome to MediFlow");

        message.setText(
                "Hello " + patientName + ",\n\n" +
                        "Welcome to MediFlow!\n\n" +
                        "Your patient registration was successful.\n\n" +
                        "Thank you."
        );

        mailSender.send(message);

        System.out.println(
                "✅ Email sent successfully to: " + to
        );
    }
}