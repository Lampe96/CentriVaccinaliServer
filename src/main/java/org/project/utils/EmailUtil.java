package org.project.utils;

import org.apache.commons.io.IOUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class EmailUtil {

    private final static String FROM = "centri.vaccinali.italiani@gmail.com";
    private final static String PASSWORD = "ProgettoCentriVaccinali";
    private final static String HOST = "smtp.gmail.com";
    private final static String PORT = "465";

    private static MimeMessage getBaseMessage() throws MessagingException {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(FROM, PASSWORD);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));

        return message;
    }

    private static void sendEmail(Message message) throws MessagingException {
        Transport.send(message);
    }

    public static void sendVerifyEmail(String to, String nickname, int code) throws MessagingException, IOException {
        MimeMessage message = getBaseMessage();
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Codice di verifica");

        String html = IOUtils.toString(Objects.requireNonNull(EmailUtil.class.getResource("verify_email_template.html")), StandardCharsets.UTF_8);
        html = html.replace("nickname", nickname);
        html = html.replace("000000", String.valueOf(code));

        message.setContent(html, "text/html");
        sendEmail(message);
    }
}
