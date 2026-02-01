package util;

import java.io.UnsupportedEncodingException;
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
    	// Ép Java sử dụng IPv4 (Cloud thường lỗi khi dùng IPv6 để gửi mail)
    	System.setProperty("java.net.preferIPv4Stack" , "true");
    	
    	String loginBrevo = System.getenv("BREVO_USER");
        String passwordBrevo = System.getenv("BREVO_PASS");
        String emailSender = System.getenv("EMAIL_USER");

        if (loginBrevo == null || passwordBrevo == null || emailSender == null) {
            System.err.println("ERROR: Biến môi trường đang bị trống!");
            return;
        }
        
        // Tạo một luồng mới để gửi email ngầm
        new Thread(() -> {
        	Properties props = new Properties();
        	props.put("mail.smtp.host", "smtp-relay.brevo.com"); // Đăng ký dùng máy chủ gửi mail là brevo
        	props.put("mail.smtp.port", "465"); // Dùng cổng 465
        	props.put("mail.smtp.auth", "true");
        	
        	// Cấu hình SSL
        	props.put("mail.smtp.ssl.enable", "true");
        	props.put("mail.smtp.socketFactory.port", "465");
        	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        	props.put("mail.smtp.socketFactory.fallback", "false");

        	// Set time out
        	props.put("mail.smtp.connectiontimeout", "15000");
        	props.put("mail.smtp.timeout", "15000");
        	props.put("mail.debug", "true");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(loginBrevo, passwordBrevo); // Login Brevo
                }
            };

            // Tạo phiên làm việc với tài khoản và mật khẩu của auth và sử dụng host là gmail của props
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            try {
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.setFrom(new InternetAddress(emailSender, "V-Note Support")); // Gửi từ email gốc
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                msg.setSubject("V-Note - Xác Thực Email", "UTF-8");
                msg.setSentDate(new java.util.Date());
                msg.setContent(content, "text/html; charset=UTF-8");

                Transport.send(msg);
                System.out.println("Email sent successfully to: " + to);
            } catch (MessagingException e) {
                System.err.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }).start(); // Kích hoạt luồng chạy ngầm
    }
    
    public static void sendEmailViaBrevoAPI(String to, String content) {
        String apiKey = System.getenv("BREVO_API_KEY");
        String senderEmail = System.getenv("EMAIL_USER");

        if (apiKey == null || senderEmail == null) {
            System.err.println("ERROR: Biến môi trường đang bị trống!");
            return;
        }

        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL("https://api.brevo.com/v3/smtp/email");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("api-key", apiKey);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // HÀM CHUẨN HÓA NỘI DUNG ĐỂ TRÁNH LỖI 400
                String escapedContent = content
                    .replace("\\", "\\\\") // Thêm xuyệt ngược cho dấu xuyệt
                    .replace("\"", "\\\"") // Thêm xuyệt ngược cho dấu ngoặc kép
                    .replace("\n", "\\n")  // Biến xuống dòng thành ký tự \n
                    .replace("\r", "\\r"); // Biến quay đầu dòng thành ký tự \r

                String jsonBody = "{"
                    + "\"sender\":{\"name\":\"V-Note Support\",\"email\":\"" + senderEmail + "\"},"
                    + "\"to\":[{\"email\":\"" + to + "\"}],"
                    + "\"subject\":\"V-Note - Xác Thực Email\","
                    + "\"htmlContent\":\"" + escapedContent + "\""
                    + "}";

                try (java.io.OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes("UTF-8"));
                }

                int responseCode = conn.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    System.out.println("Email sent successfully via API!");
                } else {
                    // Đọc thêm thông tin lỗi từ server để biết chính xác sai ở đâu
                    java.io.InputStream es = conn.getErrorStream();
                    if (es != null) {
                        try (java.util.Scanner s = new java.util.Scanner(es).useDelimiter("\\A")) {
							String errorDetails = s.hasNext() ? s.next() : "";
							System.err.println("API Error " + responseCode + ": " + errorDetails);
						}
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
	
	public static String generateOTP() {
	    int otp = (int)(Math.random() * 900000) + 100000; // Tạo số từ 100000 đến 999999
	    return String.valueOf(otp);
	}
}
