package nl.fontys.s3.carenestproject.service;

public interface MailService {
    void sendMail(String to, String subject, String body);
}
