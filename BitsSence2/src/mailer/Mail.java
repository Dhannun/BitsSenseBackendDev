/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailer;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Wecom
 */
public class Mail {

    
    public static boolean sendMail(String recepient, String otp) {

        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            Session newSession = Session.getDefaultInstance(properties, null);

            String[] emailReceipients = {recepient};  
            String emailSubject = "One Time Password :: BitsSence";
            String emailBody = "Your OTP is <u>" + otp +"</b>";
            MimeMessage mimeMessage = new MimeMessage(newSession);

            for (int i = 0; i < emailReceipients.length; i++) {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceipients[i]));
            }
            mimeMessage.setSubject(emailSubject);
            
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(emailBody, "html/text");
            MimeMultipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(bodyPart);
            mimeMessage.setContent(multiPart);

            String fromUser = "dhannunyunus@gmail.com"; 
            String fromUserPassword = "innawo4life";  
            String emailHost = "smtp.gmail.com";
            Transport transport = newSession.getTransport("smtp");
            transport.connect(emailHost, fromUser, fromUserPassword);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            System.out.println("Email successfully sent!!!");
            return true;

        } catch (Exception ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
