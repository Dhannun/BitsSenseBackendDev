/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailer;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Wecom
 */
public class JavaMailer {
    public static boolean sendMail(String recepient, String otp) {
        Properties properties = new Properties();
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        String myEmail = "dhannunyunus@gmail.com";
        String password = "innawo4life";
        
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });
        
        Message message =  prepareMessage(session, myEmail, recepient, otp);
        try {
            Transport.send(message);
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(JavaMailer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static Message prepareMessage(Session session, String myEmail, String recepient, String otp) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("One Time Password :: BitsSence");
            message.setText("Your OTP is " + otp);
            return message;
        } catch (Exception ex) {
            Logger.getLogger(JavaMailer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
