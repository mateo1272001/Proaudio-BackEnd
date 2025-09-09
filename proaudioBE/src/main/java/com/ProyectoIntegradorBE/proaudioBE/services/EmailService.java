package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String MAIL_ADDRESS;

    @Value("${spring.app-base-url}")
    private String APP_BASE_URL;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "Restablecer tu contraseña";
        String resetLink = APP_BASE_URL + "/auth/restore/password?token=" + token;
        String message = """
                Hola,
                
                Recibimos una solicitud para restablecer tu contraseña.
                Hacé clic en el siguiente enlace para continuar:
                
                %s
                
                Este enlace expirará en 30 minutos.
                
                Si no solicitaste esto, simplemente ignorá este mensaje.
                """.formatted(resetLink);

        sendSimpleEmail(toEmail, subject, message);
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(MAIL_ADDRESS);
        mailSender.send(message);
    }

    @Async
    public void sendSimpleEmailToMultipleDestinations(List<UserResponseDto> users, String subject, String text) {

        for (UserResponseDto user : users) {

            sendSimpleEmail(user.getEmail(), subject, text);

        }

    }

    public void sendMultipleEmailsToUsers(Map<String, String> userMails, String title) {

        userMails.forEach((mail, body) -> {

            System.out.println("Enviando mail diario a: " + mail + " -> " + body);

            sendSimpleEmail(mail, title, body);

        });

    }
}
