package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
	public static String toSHA1 (String input) {
		try {
			// MessageDigest la class chua nhieu bo thuat toan ma hoa, de khoi tao ta can chon bo thuat toan ta can
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] sha1 = md.digest(input.getBytes()); // method digest(byte[]): byte[]; thuc hien bam theo thuat toan
			
			// Vi thuat toan SHA-1 ma hoa thanh cac ky tu dat biet nen ta nen chuyen no ve so HEX
			StringBuilder sb = new StringBuilder();
			for (byte b : sha1) {
				sb.append(String.format("%02x", b));
				// Vi mot byte co gia tri tu 0-255 tuong duong voi 0-ff trong HEX nen ta can 2 chu so, neu khong du 2 chu so thi them so 0; x la dinh dang cua hexadecimal.
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}
