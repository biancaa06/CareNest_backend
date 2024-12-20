package nl.fontys.s3.carenestproject.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendMail(String to, String subject, String body);
    void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException;
}
