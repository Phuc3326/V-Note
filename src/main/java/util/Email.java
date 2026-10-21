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
