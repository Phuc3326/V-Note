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
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            
            // Kích hoạt SSL cho cổng 465
            props.put("mail.smtp.ssl.enable", "true"); 
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // Thêm Timeout để tránh việc luồng này bị treo vĩnh viễn
            props.put("mail.smtp.connectiontimeout", "5000"); // 5 giây
            props.put("mail.smtp.timeout", "5000"); // 5 giây
            // Bật Debug để xem chi tiết lỗi trong Log của Railway
            props.put("mail.debug", "true");

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
