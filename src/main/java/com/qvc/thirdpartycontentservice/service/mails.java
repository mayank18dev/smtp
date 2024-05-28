package com.qvc.thirdpartycontentservice.service;

import java.util.Properties;
import com.sun.mail.smtp.SMTPTransport;


import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static java.util.Base64.getEncoder;

public class mails {


    public static void sendMail(String accessToken) throws MessagingException {



        sendMail("@adobe.com",
                accessToken,
                "@adobe.com",
                "smtp.office365.com",
                "587") ;
    }

    public static String tokenforsmtp(String userName, String accessToken) {
        final String ctrlA = Character.toString((char) 1);
        final String coded = "user=" + userName + ctrlA + "auth=Bearer " + accessToken + ctrlA + ctrlA;
        return getEncoder().encodeToString(coded.getBytes());
    }

    private static void sendMail(
            String tos,
            String token,
            String username,
            String host,
            String port) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth.xoauth2.disable", "false");
        props.put("mail.smtp.sasl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.imap.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "false");
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props);
        try {
            session.setDebug(true);
            Message m1 = testMessage(username, session, tos);
            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            transport.connect(host, username, null);
            transport.issueCommand("AUTH XOAUTH2 " + token, 235);
            transport.sendMessage(m1, m1.getAllRecipients());
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public static Message testMessage(String from, Session session, String tos) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress recipients[] = InternetAddress.parse(tos);
            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setSubject("Mail Send Successful");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("This is the message body");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            return message;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws MessagingException {


        // Access token (OAuth token)
        String accessToken = "";
         sendMail(accessToken);

    }
}
