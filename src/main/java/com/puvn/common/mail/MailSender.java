package com.puvn.common.mail;

import com.puvn.models.UserClass;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    private Properties properties = new Properties();

    public MailSender() {
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.properties.put("mail.smtp.host", "smtp.gmail.com");
        this.properties.put("mail.smtp.port", "587");
        this.properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    private void send(UserClass recipient, String subject, String message) {
        Session session = Session.getInstance(this.properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication();
            }
        });

        try {
            Message mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(""));
            mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.getEmail()));
            mail.setSubject(subject);
            mail.setText(message);
            Transport.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMail(MailTemplate mailTemplate) {
        send(mailTemplate.getRecipient(), mailTemplate.getSubject(), mailTemplate.getMessage());
    }
}



