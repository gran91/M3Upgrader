package mail;

import java.util.Properties;
import javax.mail.Address;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.app.main.Ressource;
import java.io.File;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class GmailTLS {

    private final Session session;

    public GmailTLS() {
        final String username = "m3upgrader@gmail.com";
        final String password = "3KleS2014";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public Message writeMail(List<String> mailTo, String subject, String msg) throws AddressException, MessagingException {
        Address[] listAddress = new Address[mailTo.size()];
        for (int i = 0; i < mailTo.size(); i++) {
            listAddress[i] = new InternetAddress(mailTo.get(i));
        }
        return writeMail(listAddress, subject, msg);
    }

    public Message writeMail(String mailTo, String subject, String msg) throws AddressException, MessagingException {
        return writeMail(new InternetAddress(mailTo), subject, msg);
    }

    public Message writeMail(Address mailTo, String subject, String msg) throws MessagingException {
        return writeMail(new Address[]{mailTo}, subject, msg);
    }

    public Message writeMail(Address[] mailTo, String subject, String msg) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Ressource.title.toLowerCase() + "@3kles-consulting.com"));
        message.setRecipients(Message.RecipientType.TO, mailTo);
        message.setSubject(subject);
        message.setText(msg);
        return message;
    }

    public Message addAttachment(Message msg, File f) throws MessagingException {
        return addAttachment(msg, f, f.getName());
    }

    public Message addAttachment(Message msg, File f, String fileDesc) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        messageBodyPart = new MimeBodyPart();
        String fileName = fileDesc;
        DataSource source = new FileDataSource(f);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        return msg;
    }

    public void sendMail(Message msg) throws MessagingException {
        Transport.send(msg);
    }
}
