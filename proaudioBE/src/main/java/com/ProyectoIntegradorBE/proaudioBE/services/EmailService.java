package com.ProyectoIntegradorBE.proaudioBE.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
                
                Saludos,
                Marcos y Mate. Unos tipazos.
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
}
