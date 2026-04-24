package services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        System.out.println("MOCK EMAIL SENT TO: " + toEmail);
        System.out.println("RESET LINK: " + resetLink);
    }
}
