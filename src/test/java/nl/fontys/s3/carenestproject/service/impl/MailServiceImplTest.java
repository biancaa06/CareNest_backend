package nl.fontys.s3.carenestproject.service.impl;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private JavaMailSender mockMailSender;

    private MailServiceImpl mailServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        mailServiceImplUnderTest = new MailServiceImpl(mockMailSender);
    }

    @Test
    void testSendMail() {
        // Setup
        // Run the test
        mailServiceImplUnderTest.sendMail("to", "subject", "text");

        // Verify the results
        // Confirm JavaMailSender.send(...).
        final SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("carenest06@gmail.com");
        simpleMessage.setTo("to");
        simpleMessage.setSubject("subject");
        simpleMessage.setText("text");
        verify(mockMailSender).send(simpleMessage);
    }

    @Test
    void testSendMail_JavaMailSenderThrowsMailException() {
        // Setup
        final SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("carenest06@gmail.com");
        simpleMessage.setTo("to");
        simpleMessage.setSubject("subject");
        simpleMessage.setText("text");

        // Use a concrete subclass of MailException
        doThrow(new MailSendException("Test exception"))
                .when(mockMailSender).send(any(SimpleMailMessage.class));

        // Run the test
        assertThatThrownBy(() -> mailServiceImplUnderTest.sendMail("to", "subject", "text"))
                .isInstanceOf(org.springframework.mail.MailSendException.class)
                .hasMessageContaining("Test exception");
    }

    @Test
    void testSendHtmlEmail() throws Exception {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Run the test
        mailServiceImplUnderTest.sendHtmlEmail("to", "subject", "htmlBody");

        // Verify the results
        verify(mockMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendHtmlEmail_JavaMailSenderSendThrowsMailException() {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new MailSendException("Test exception"))
                .when(mockMailSender).send(any(MimeMessage.class));

        // Run the test
        assertThatThrownBy(() -> mailServiceImplUnderTest.sendHtmlEmail("to", "subject", "text"))
                .isInstanceOf(org.springframework.mail.MailSendException.class)
                .hasMessageContaining("Test exception");
    }
}
