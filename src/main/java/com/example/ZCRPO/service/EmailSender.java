package com.example.ZCRPO.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String recipientEmail, String confirmationCode) {
        final String username = "";
        final String password = "";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Confirmation Code");
            message.setText("Your confirmation code is: " + confirmationCode);

            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendConfirmationEmail(String recipientEmail, String confirmationCode) {
        EmailSender.sendEmail(recipientEmail, confirmationCode);
    }
}