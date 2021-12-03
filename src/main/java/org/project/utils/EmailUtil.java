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

/**
 * Classe utilizzata per l'invio della email di verifica all'utente indicato
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class EmailUtil {

    /**
     * Account utilizzato per inviare le email
     */
    private final static String FROM = "centri.vaccinali.italiani@gmail.com";
    /**
     * Password del account utilizzato
     */
    private final static String PASSWORD = "ProgettoCentriVaccinali";
    /**
     * Host utilizzato per l'invio della email
     */
    private final static String HOST = "smtp.gmail.com";
    /**
     * Porta utilizzata per l'invio della email
     */
    private final static String PORT = "465";
    /**
     * Sessione per l'invio dei messaggi
     */
    private static Session session = null;

    /**
     * Utilizzato per settare le proprieta' di sistema e reperire la sessione,
     * effettuando l'autenticazione sull'account prestabilito, per l'invio dei messaggi
     */
    private static void setSysPropertiesAndGetSession() {
        if (session == null) {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", HOST);
            properties.put("mail.smtp.port", PORT);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(FROM, PASSWORD);
                }
            });
        }
    }

    /**
     * Utilizzato per settare in un messaggio di base la sessione da utilizzare
     * e la email mittente
     *
     * @return Restituisce un MineMessage di base
     * @throws MessagingException MessagingException
     */
    private static MimeMessage getBaseMessage() throws MessagingException {
        setSysPropertiesAndGetSession();

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));

        return message;
    }

    /**
     * Utilizzato per inviare il messaggio
     *
     * @param message messaggio da inviare
     * @throws MessagingException MessagingException
     */
    private static void sendEmail(Message message) throws MessagingException {
        Transport.send(message);
    }

    /**
     * Utilizzato per settare nel messaggio la email del destinatario e,
     * inserire nel template html il nickname del utente e il codice di verifica
     *
     * @param to       email destinatario
     * @param nickname nickname
     * @param code     codice di verifica
     * @throws MessagingException MessagingException
     * @throws IOException        IOException
     */
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