package services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // private final JavaMailSender mailSender;
    // public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        // Here you would use JavaMailSender
        // For now, logging to console as mock
        System.out.println("----------------------------------------------------------");
        System.out.println("MOCK EMAIL SENT TO: " + toEmail);
        System.out.println("RESET LINK: " + resetLink);
        System.out.println("----------------------------------------------------------");
    }
}
