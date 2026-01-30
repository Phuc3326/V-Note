package util;

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
    public static void sendEmail(String to, String content) {
    	String from = System.getenv("EMAIL_USER");
        String password = System.getenv("EMAIL_APP_PASS");

        if (from == null || password == null) {
            System.err.println("ERROR: Biến môi trường EMAIL_USER hoặc EMAIL_APP_PASS đang bị trống!");
            return;
        }
        
        // Tạo một luồng mới để gửi email ngầm
        new Thread(() -> {
        	Properties props = new Properties();
        	props.put("mail.smtp.host", "smtp.gmail.com"); // Đăng ký dùng máy chủ gửi mail là gmail
        	props.put("mail.smtp.port", "587"); // Dùng cổng 587
        	props.put("mail.smtp.auth", "true");
        	props.put("mail.smtp.starttls.enable", "true"); // Bật TLS
        	props.put("mail.smtp.starttls.required", "true");

        	// Set time out
        	props.put("mail.smtp.connectiontimeout", "10000");
        	props.put("mail.smtp.timeout", "10000");
        	props.put("mail.debug", "true");

        	// Ép Java sử dụng IPv4 (Cloud thường lỗi khi dùng IPv6 để gửi mail)
        	System.setProperty("java.net.preferIPv4Stack" , "true");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            };

            // Tạo phiên làm việc với tài khoản và mật khẩu của auth và sử dụng host là gmail của props
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            try {
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.setFrom(new InternetAddress(from)); // Dùng InternetAddress sẽ chuẩn hơn
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                msg.setSubject("V-Note - Xác Thực Email", "UTF-8");
                msg.setSentDate(new java.util.Date());
                msg.setContent(content, "text/html; charset=UTF-8");

                Transport.send(msg);
                System.out.println("Email sent successfully to: " + to);
            } catch (MessagingException e) {
                System.err.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
            }
        }).start(); // Kích hoạt luồng chạy ngầm
    }
	
	public static String generateOTP() {
	    int otp = (int)(Math.random() * 900000) + 100000; // Tạo số từ 100000 đến 999999
	    return String.valueOf(otp);
	}
}
