package util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	// Email: huynhhuuphuc3326@gmail.com
	// Password: zgyiekckyppsclqw
	static final String from = "huynhhuuphuc3326@gmail.com";
	static final String password = "zgyiekckyppsclqw";
	
	public static void sendEmail (String to, String content) {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		};
		
		// Phien lam viec
		Session session = Session.getInstance(props, auth);
		
		// Tao mot tin nhan
		MimeMessage msg = new MimeMessage(session);
		
		try {
			// Kieu noi dung
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			// Nguoi gui
			msg.setFrom(from);
			// Nguoi nhan
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			// Tieu de email
			msg.setSubject("V-Note - Xác Thực Email");
			// Ngay gui
			msg.setSentDate(new Date());
			// Noi dung
			msg.setContent(content, "text/html; charset=UTF-8");
			
			// Gui email
			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static String generateOTP() {
	    int otp = (int)(Math.random() * 900000) + 100000; // Tạo số từ 100000 đến 999999
	    return String.valueOf(otp);
	}
}
