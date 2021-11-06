package org.project.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

    public static void sendVerifyEmail(String to, String nickname, int code) throws MessagingException {
        MimeMessage message = getBaseMessage();
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Codice di verifica");

        String html =
                "<center>\n" +
                        "  <center style=\"background-color: #cccccc; min-width: 580px; width: 50%\">\n" +
                        "    <table\n" +
                        "      style=\"\n" +
                        "        margin: 0 auto;\n" +
                        "        border-collapse: collapse;\n" +
                        "        border-spacing: 0;\n" +
                        "        float: none;\n" +
                        "        margin: 0 auto;\n" +
                        "        padding: 0;\n" +
                        "        text-align: center;\n" +
                        "        vertical-align: top;\n" +
                        "        width: 100%;\n" +
                        "      \"\n" +
                        "    >\n" +
                        "      <tbody>\n" +
                        "        <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "          <td\n" +
                        "            height=\"15px\"\n" +
                        "            style=\"\n" +
                        "              margin: 0;\n" +
                        "              border-collapse: collapse !important;\n" +
                        "              color: #322f37;\n" +
                        "              font-family: Helvetica, Arial, sans-serif;\n" +
                        "              font-size: 10px;\n" +
                        "              font-weight: 400;\n" +
                        "              line-height: 10px;\n" +
                        "              margin: 0;\n" +
                        "              padding: 0;\n" +
                        "              text-align: left;\n" +
                        "              vertical-align: top;\n" +
                        "              word-wrap: break-word;\n" +
                        "            \"\n" +
                        "          >\n" +
                        "            &nbsp;\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tbody>\n" +
                        "    </table>\n" +
                        "    <table\n" +
                        "      align=\"center\"\n" +
                        "      class=\"m_-5056136103504263480container\"\n" +
                        "      style=\"\n" +
                        "        margin: 0 auto;\n" +
                        "        background: #fff;\n" +
                        "        border-collapse: collapse;\n" +
                        "        border-spacing: 0;\n" +
                        "        float: none;\n" +
                        "        margin: 0 auto;\n" +
                        "        padding: 10px;\n" +
                        "        text-align: center;\n" +
                        "        vertical-align: top;\n" +
                        "        width: 550px;\n" +
                        "        margin-left: 10px !important;\n" +
                        "        margin-right: 10px !important;\n" +
                        "      \"\n" +
                        "    >\n" +
                        "      <tbody>\n" +
                        "        <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "          <td\n" +
                        "            style=\"\n" +
                        "              margin: 0;\n" +
                        "              border-collapse: collapse !important;\n" +
                        "              color: #322f37;\n" +
                        "              font-family: Helvetica, Arial, sans-serif;\n" +
                        "              font-size: 16px;\n" +
                        "              font-weight: 400;\n" +
                        "              line-height: 1.3;\n" +
                        "              margin: 0;\n" +
                        "              padding: 0;\n" +
                        "              text-align: left;\n" +
                        "              vertical-align: top;\n" +
                        "              word-wrap: break-word;\n" +
                        "            \"\n" +
                        "          >\n" +
                        "            <table\n" +
                        "              class=\"m_-5056136103504263480row m_-5056136103504263480header-v2\"\n" +
                        "              style=\"\n" +
                        "                background-color: #fff;\n" +
                        "                background-image: none;\n" +
                        "                background-position: top left;\n" +
                        "                background-repeat: repeat;\n" +
                        "                border-bottom: 1px solid #efeef1;\n" +
                        "                border-collapse: collapse;\n" +
                        "                border-spacing: 0;\n" +
                        "                display: table;\n" +
                        "                margin: 10px 0 15px 0;\n" +
                        "                padding: 0;\n" +
                        "                text-align: left;\n" +
                        "                vertical-align: top;\n" +
                        "                width: 100%;\n" +
                        "              \"\n" +
                        "            >\n" +
                        "              <tbody>\n" +
                        "                <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "                  <th\n" +
                        "                    class=\"\n" +
                        "                      m_-5056136103504263480small-12\n" +
                        "                      m_-5056136103504263480columns\n" +
                        "                    \"\n" +
                        "                    style=\"\n" +
                        "                      margin: 0 auto;\n" +
                        "                      color: #322f37;\n" +
                        "                      font-family: Helvetica, Arial, sans-serif;\n" +
                        "                      font-size: 16px;\n" +
                        "                      font-weight: 400;\n" +
                        "                      line-height: 1.3;\n" +
                        "                      margin: 0 auto;\n" +
                        "                      padding: 0;\n" +
                        "                      padding-bottom: 0 !important;\n" +
                        "                      padding-left: 20px;\n" +
                        "                      padding-right: 20px;\n" +
                        "                      padding-top: 0 !important;\n" +
                        "                      text-align: left;\n" +
                        "                      width: 560px;\n" +
                        "                    \"\n" +
                        "                  >\n" +
                        "                    <table\n" +
                        "                      style=\"\n" +
                        "                        border-collapse: collapse;\n" +
                        "                        border-spacing: 0;\n" +
                        "                        padding: 0;\n" +
                        "                        text-align: left;\n" +
                        "                        vertical-align: top;\n" +
                        "                        width: 100%;\n" +
                        "                      \"\n" +
                        "                    >\n" +
                        "                      <tbody>\n" +
                        "                        <tr\n" +
                        "                          style=\"\n" +
                        "                            padding: 0;\n" +
                        "                            text-align: left;\n" +
                        "                            vertical-align: top;\n" +
                        "                            border-bottom: 1px solid #e51877;\n" +
                        "                          \"\n" +
                        "                        >\n" +
                        "                          <td style=\"width: 25%\">\n" +
                        "                            <img\n" +
                        "                              src=\"https://www.uslumbria2.it/MC-API/Risorse/StreamRisorsa.ashx?guid=40ad28fd-a282-4286-95fb-82598c2ccd10\"\n" +
                        "                              alt=\"Primula\"\n" +
                        "                              style=\"\n" +
                        "                                clear: both;\n" +
                        "                                max-width: 100%;\n" +
                        "                                padding: 10px;\n" +
                        "                                float: right;\n" +
                        "                                text-align: center;\n" +
                        "                                width: 95px !important;\n" +
                        "                                height: 90px !important;\n" +
                        "                                vertical-align: middle;\n" +
                        "                              \"\n" +
                        "                              class=\"CToWUd\"\n" +
                        "                            />\n" +
                        "                          </td>\n" +
                        "                          <td>\n" +
                        "                            <h1\n" +
                        "                              style=\"\n" +
                        "                                text-align: left;\n" +
                        "                                vertical-align: middle;\n" +
                        "                                margin: 37px 0px;\n" +
                        "                              \"\n" +
                        "                            >\n" +
                        "                              Centri vaccinali italiani\n" +
                        "                            </h1>\n" +
                        "                          </td>\n" +
                        "                        </tr>\n" +
                        "                      </tbody>\n" +
                        "                    </table>\n" +
                        "                  </th>\n" +
                        "                </tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "            <table\n" +
                        "              style=\"\n" +
                        "                border-collapse: collapse;\n" +
                        "                border-spacing: 0;\n" +
                        "                padding: 0;\n" +
                        "                text-align: left;\n" +
                        "                vertical-align: top;\n" +
                        "                width: 100%;\n" +
                        "              \"\n" +
                        "            >\n" +
                        "              <tbody>\n" +
                        "                <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "                  <td\n" +
                        "                    height=\"15px\"\n" +
                        "                    style=\"\n" +
                        "                      margin: 0;\n" +
                        "                      border-collapse: collapse !important;\n" +
                        "                      color: #322f37;\n" +
                        "                      font-family: Helvetica, Arial, sans-serif;\n" +
                        "                      font-size: 15px;\n" +
                        "                      font-weight: 400;\n" +
                        "                      line-height: 15px;\n" +
                        "                      margin: 0;\n" +
                        "                      padding: 0;\n" +
                        "                      text-align: left;\n" +
                        "                      vertical-align: top;\n" +
                        "                      word-wrap: break-word;\n" +
                        "                    \"\n" +
                        "                  >\n" +
                        "                    &nbsp;\n" +
                        "                  </td>\n" +
                        "                </tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "            <table\n" +
                        "              class=\"m_-5056136103504263480row\"\n" +
                        "              style=\"\n" +
                        "                border-collapse: collapse;\n" +
                        "                border-spacing: 0;\n" +
                        "                display: table;\n" +
                        "                padding: 0;\n" +
                        "                text-align: left;\n" +
                        "                vertical-align: top;\n" +
                        "                width: 100%;\n" +
                        "              \"\n" +
                        "            >\n" +
                        "              <tbody>\n" +
                        "                <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "                  <th\n" +
                        "                    class=\"\n" +
                        "                      m_-5056136103504263480small-12\n" +
                        "                      m_-5056136103504263480columns\n" +
                        "                    \"\n" +
                        "                    style=\"\n" +
                        "                      margin: 0 auto;\n" +
                        "                      color: #322f37;\n" +
                        "                      font-family: Helvetica, Arial, sans-serif;\n" +
                        "                      font-size: 18px;\n" +
                        "                      font-weight: 500;\n" +
                        "                      line-height: 1.3;\n" +
                        "                      margin: 0 auto;\n" +
                        "                      padding: 0;\n" +
                        "                      padding-bottom: 0 !important;\n" +
                        "                      padding-left: 20px;\n" +
                        "                      padding-right: 20px;\n" +
                        "                      padding-top: 0 !important;\n" +
                        "                      text-align: left;\n" +
                        "                      width: 560px;\n" +
                        "                    \"\n" +
                        "                  >\n" +
                        "                    <table\n" +
                        "                      style=\"\n" +
                        "                        border-collapse: collapse;\n" +
                        "                        border-spacing: 0;\n" +
                        "                        padding: 0;\n" +
                        "                        text-align: left;\n" +
                        "                        vertical-align: top;\n" +
                        "                        width: 100%;\n" +
                        "                      \"\n" +
                        "                    >\n" +
                        "                      <tbody>\n" +
                        "                        <tr\n" +
                        "                          style=\"\n" +
                        "                            padding: 0;\n" +
                        "                            text-align: left;\n" +
                        "                            vertical-align: top;\n" +
                        "                          \"\n" +
                        "                        >\n" +
                        "                          <th\n" +
                        "                            style=\"\n" +
                        "                              margin: 0;\n" +
                        "                              color: #322f37;\n" +
                        "                              font-family: Helvetica, Arial, sans-serif;\n" +
                        "                              font-size: 18px;\n" +
                        "                              font-weight: 500;\n" +
                        "                              line-height: 1.3;\n" +
                        "                              margin: 0;\n" +
                        "                              padding: 0;\n" +
                        "                              text-align: left;\n" +
                        "                            \"\n" +
                        "                          >\n" +
                        "                            <h6\n" +
                        "                              style=\"\n" +
                        "                                margin: 0;\n" +
                        "                                margin-bottom: 10px;\n" +
                        "                                color: inherit;\n" +
                        "                                font-family: Helvetica, Arial, sans-serif;\n" +
                        "                                font-size: 18px;\n" +
                        "                                font-weight: 500;\n" +
                        "                                line-height: 1.3;\n" +
                        "                                margin: 0;\n" +
                        "                                margin-bottom: 0;\n" +
                        "                                padding: 0;\n" +
                        "                                padding-bottom: 0;\n" +
                        "                                text-align: center;\n" +
                        "                                word-wrap: normal;\n" +
                        "                                color: #e51877;\n" +
                        "                              \"\n" +
                        "                            >\n" +
                        "                              Ciao " + nickname + ",\n" +
                        "                            </h6>\n" +
                        "                          </th>\n" +
                        "                          <th\n" +
                        "                            style=\"\n" +
                        "                              margin: 0;\n" +
                        "                              color: #322f37;\n" +
                        "                              font-family: Helvetica, Arial, sans-serif;\n" +
                        "                              font-size: 16px;\n" +
                        "                              font-weight: 400;\n" +
                        "                              line-height: 1.3;\n" +
                        "                              margin: 0;\n" +
                        "                              padding: 0 !important;\n" +
                        "                              text-align: left;\n" +
                        "                              width: 0;\n" +
                        "                            \"\n" +
                        "                          ></th>\n" +
                        "                        </tr>\n" +
                        "                      </tbody>\n" +
                        "                    </table>\n" +
                        "                  </th>\n" +
                        "                </tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "            <table\n" +
                        "              class=\"m_-5056136103504263480row\"\n" +
                        "              style=\"\n" +
                        "                border-collapse: collapse;\n" +
                        "                border-spacing: 0;\n" +
                        "                display: table;\n" +
                        "                padding: 0;\n" +
                        "                text-align: left;\n" +
                        "                vertical-align: top;\n" +
                        "                width: 100%;\n" +
                        "              \"\n" +
                        "            >\n" +
                        "              <tbody>\n" +
                        "                <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "                  <th\n" +
                        "                    class=\"\n" +
                        "                      m_-5056136103504263480small-12\n" +
                        "                      m_-5056136103504263480columns\n" +
                        "                    \"\n" +
                        "                    style=\"\n" +
                        "                      margin: 0 auto;\n" +
                        "                      color: #322f37;\n" +
                        "                      font-family: Helvetica, Arial, sans-serif;\n" +
                        "                      font-size: 16px;\n" +
                        "                      font-weight: 300;\n" +
                        "                      line-height: 1.3;\n" +
                        "                      margin: 0 auto;\n" +
                        "                      padding: 0;\n" +
                        "                      padding-bottom: 16px;\n" +
                        "                      padding-left: 20px;\n" +
                        "                      padding-right: 20px;\n" +
                        "                      padding-top: 10px;\n" +
                        "                      text-align: left;\n" +
                        "                      width: 560px;\n" +
                        "                    \"\n" +
                        "                  >\n" +
                        "                    <table\n" +
                        "                      style=\"\n" +
                        "                        border-collapse: collapse;\n" +
                        "                        border-spacing: 0;\n" +
                        "                        padding: 0;\n" +
                        "                        text-align: left;\n" +
                        "                        vertical-align: top;\n" +
                        "                        width: 100%;\n" +
                        "                      \"\n" +
                        "                    >\n" +
                        "                      <tbody>\n" +
                        "                        <tr\n" +
                        "                          style=\"\n" +
                        "                            padding: 0;\n" +
                        "                            text-align: left;\n" +
                        "                            vertical-align: top;\n" +
                        "                          \"\n" +
                        "                        >\n" +
                        "                          <th\n" +
                        "                            style=\"\n" +
                        "                              margin: 0;\n" +
                        "                              color: #322f37;\n" +
                        "                              font-family: Helvetica, Arial, sans-serif;\n" +
                        "                              font-size: 16px;\n" +
                        "                              font-weight: 400;\n" +
                        "                              line-height: 1.3;\n" +
                        "                              margin: 0;\n" +
                        "                              padding: 0;\n" +
                        "                              text-align: left;\n" +
                        "                            \"\n" +
                        "                          >\n" +
                        "                            <p\n" +
                        "                              style=\"\n" +
                        "                                margin: 0;\n" +
                        "                                margin-bottom: 10px;\n" +
                        "                                font-family: Helvetica, Arial, Verdana,\n" +
                        "                                  'Trebuchet MS';\n" +
                        "                                font-size: 16px;\n" +
                        "                                font-weight: 300;\n" +
                        "                                line-height: 24px;\n" +
                        "                                margin: 0;\n" +
                        "                                margin-bottom: 0;\n" +
                        "                                padding: 0;\n" +
                        "                                padding-bottom: 0;\n" +
                        "                                text-align: center;\n" +
                        "                              \"\n" +
                        "                            >\n" +
                        "                              per completare la registrazione, immetti il codice\n" +
                        "                              seguente:\n" +
                        "                            </p>\n" +
                        "                          </th>\n" +
                        "                          <th\n" +
                        "                            style=\"\n" +
                        "                              margin: 0;\n" +
                        "                              color: #322f37;\n" +
                        "                              font-family: Helvetica, Arial, sans-serif;\n" +
                        "                              font-size: 14px;\n" +
                        "                              font-weight: 400;\n" +
                        "                              line-height: 1.3;\n" +
                        "                              margin: 0;\n" +
                        "                              padding: 0 !important;\n" +
                        "                              text-align: left;\n" +
                        "                              width: 0;\n" +
                        "                            \"\n" +
                        "                          ></th>\n" +
                        "                        </tr>\n" +
                        "                      </tbody>\n" +
                        "                    </table>\n" +
                        "                  </th>\n" +
                        "                </tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "            <table\n" +
                        "              class=\"\n" +
                        "                m_-5056136103504263480row\n" +
                        "                m_-5056136103504263480hide-for-large\n" +
                        "                m_-5056136103504263480app-store-section\n" +
                        "              \"\n" +
                        "              style=\"\n" +
                        "                border-collapse: collapse;\n" +
                        "                border-spacing: 0;\n" +
                        "                border-top: #efeef1 1px solid;\n" +
                        "                font-size: 0;\n" +
                        "                line-height: 0;\n" +
                        "                max-height: 0;\n" +
                        "                overflow: hidden;\n" +
                        "                padding: 0;\n" +
                        "                text-align: left;\n" +
                        "                vertical-align: top;\n" +
                        "                width: 100%;\n" +
                        "              \"\n" +
                        "            >\n" +
                        "              <tbody>\n" +
                        "                <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "                  <th\n" +
                        "                    class=\"\n" +
                        "                      m_-5056136103504263480small-12\n" +
                        "                      m_-5056136103504263480columns\n" +
                        "                    \"\n" +
                        "                    style=\"\n" +
                        "                      margin: 0 auto;\n" +
                        "                      font-family: Helvetica, Arial, sans-serif;\n" +
                        "                      font-size: 16px;\n" +
                        "                      font-weight: 300;\n" +
                        "                      line-height: 1.3;\n" +
                        "                      margin: 0 auto;\n" +
                        "                      padding: 0;\n" +
                        "                      padding-bottom: 16px;\n" +
                        "                      padding-left: 20px;\n" +
                        "                      padding-right: 20px;\n" +
                        "                      padding-top: 27px;\n" +
                        "                      text-align: left;\n" +
                        "                      width: 560px;\n" +
                        "                    \"\n" +
                        "                  >\n" +
                        "                    <table\n" +
                        "                      style=\"\n" +
                        "                        border-collapse: collapse;\n" +
                        "                        border-spacing: 0;\n" +
                        "                        padding: 0;\n" +
                        "                        text-align: left;\n" +
                        "                        vertical-align: top;\n" +
                        "                        width: 100%;\n" +
                        "                      \"\n" +
                        "                    >\n" +
                        "                      <tbody>\n" +
                        "                        <tr\n" +
                        "                          style=\"\n" +
                        "                            padding: 0;\n" +
                        "                            text-align: left;\n" +
                        "                            vertical-align: top;\n" +
                        "                          \"\n" +
                        "                        >\n" +
                        "                          <th\n" +
                        "                            style=\"\n" +
                        "                              margin: 0;\n" +
                        "                              color: #322f37;\n" +
                        "                              font-family: Helvetica, Arial, sans-serif;\n" +
                        "                              font-size: 16px;\n" +
                        "                              font-weight: 300;\n" +
                        "                              line-height: 1.3;\n" +
                        "                              margin: 0;\n" +
                        "                              padding: 0;\n" +
                        "                              text-align: left;\n" +
                        "                            \"\n" +
                        "                          >\n" +
                        "                            <div\n" +
                        "                              style=\"\n" +
                        "                                margin: 0;\n" +
                        "                                margin-bottom: 10px;\n" +
                        "                                color: #322f37;\n" +
                        "                                font-family: Helvetica, Arial, Verdana,\n" +
                        "                                  'Trebuchet MS';\n" +
                        "                                font-size: 32px;\n" +
                        "                                font-weight: 400;\n" +
                        "                                line-height: 24px;\n" +
                        "                                margin: 0;\n" +
                        "                                margin-bottom: 0;\n" +
                        "                                padding: 0;\n" +
                        "                                padding-bottom: 0;\n" +
                        "                                text-align: center;\n" +
                        "                                padding-top: 5px;\n" +
                        "                              \"\n" +
                        "                            >\n" +
                        "                              <p\n" +
                        "                                style=\"\n" +
                        "                                  background: #faf9fa;\n" +
                        "                                  border: 1px solid;\n" +
                        "                                  border-style: solid;\n" +
                        "                                  border-color: #dad8de;\n" +
                        "                                  display: inline;\n" +
                        "                                  padding-bottom: 5px;\n" +
                        "                                  padding-left: 5px;\n" +
                        "                                  padding-right: 5px;\n" +
                        "                                  padding-top: 5px;\n" +
                        "                                \"\n" +
                        "                              >\n" +
                        "                                " + code + "\n" +
                        "                              </p>\n" +
                        "                            </div>\n" +
                        "                            <table\n" +
                        "                              style=\"\n" +
                        "                                border-collapse: collapse;\n" +
                        "                                border-spacing: 0;\n" +
                        "                                padding: 0;\n" +
                        "                                text-align: left;\n" +
                        "                                vertical-align: top;\n" +
                        "                                width: 100%;\n" +
                        "                              \"\n" +
                        "                            >\n" +
                        "                              <tbody>\n" +
                        "                                <tr\n" +
                        "                                  style=\"\n" +
                        "                                    padding: 0;\n" +
                        "                                    text-align: left;\n" +
                        "                                    vertical-align: top;\n" +
                        "                                  \"\n" +
                        "                                >\n" +
                        "                                  <td\n" +
                        "                                    height=\"20px\"\n" +
                        "                                    style=\"\n" +
                        "                                      margin: 0;\n" +
                        "                                      border-collapse: collapse !important;\n" +
                        "                                      color: #322f37;\n" +
                        "                                      font-family: Helvetica, Arial, sans-serif;\n" +
                        "                                      font-size: 10px;\n" +
                        "                                      font-weight: 400;\n" +
                        "                                      line-height: 10px;\n" +
                        "                                      margin: 0;\n" +
                        "                                      padding: 0;\n" +
                        "                                      text-align: left;\n" +
                        "                                      vertical-align: top;\n" +
                        "                                      word-wrap: break-word;\n" +
                        "                                    \"\n" +
                        "                                  >\n" +
                        "                                    &nbsp;\n" +
                        "                                  </td>\n" +
                        "                                </tr>\n" +
                        "                              </tbody>\n" +
                        "                            </table>\n" +
                        "                          </th>\n" +
                        "                        </tr>\n" +
                        "                      </tbody>\n" +
                        "                    </table>\n" +
                        "                  </th>\n" +
                        "                </tr>\n" +
                        "              </tbody>\n" +
                        "            </table>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tbody>\n" +
                        "    </table>\n" +
                        "    <table\n" +
                        "      style=\"\n" +
                        "        margin: 0 auto;\n" +
                        "        border-collapse: collapse;\n" +
                        "        border-spacing: 0;\n" +
                        "        float: none;\n" +
                        "        margin: 0 auto;\n" +
                        "        padding: 0;\n" +
                        "        text-align: center;\n" +
                        "        vertical-align: top;\n" +
                        "        width: 100%;\n" +
                        "      \"\n" +
                        "    >\n" +
                        "      <tbody>\n" +
                        "        <tr style=\"padding: 0; text-align: left; vertical-align: top\">\n" +
                        "          <td\n" +
                        "            height=\"15px\"\n" +
                        "            style=\"\n" +
                        "              margin: 0;\n" +
                        "              border-collapse: collapse !important;\n" +
                        "              color: #322f37;\n" +
                        "              font-family: Helvetica, Arial, sans-serif;\n" +
                        "              font-size: 10px;\n" +
                        "              font-weight: 400;\n" +
                        "              line-height: 10px;\n" +
                        "              margin: 0;\n" +
                        "              padding: 0;\n" +
                        "              text-align: left;\n" +
                        "              vertical-align: top;\n" +
                        "              word-wrap: break-word;\n" +
                        "            \"\n" +
                        "          >\n" +
                        "            &nbsp;\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tbody>\n" +
                        "    </table>\n" +
                        "  </center>\n" +
                        "</center>\n";

        message.setContent(html, "text/html");
        sendEmail(message);
    }
}
