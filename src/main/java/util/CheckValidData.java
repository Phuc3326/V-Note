package util;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckValidData {
	public static boolean isValidFullName (String fullName) {
		return !(fullName.trim().isEmpty());
	}
	public static boolean isValidUserName (String userName) {
		return !(userName.trim().isEmpty());
	}
	public static boolean isValidPassword (String password) {
		return password.length() >= 8;
	}
	
	public static boolean isValidReEnterPassword (String password, String reEnterPassword) {
		return password.equals(reEnterPassword);
	}
	
	public static boolean isValidEmail (String email) {
		final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static boolean isValidPhone (String phone) {
		if (phone.trim().isEmpty()) return true;
		final String PHONE_REGEX = "^0\\d{9}$";
		final Pattern pattern = Pattern.compile(PHONE_REGEX);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}
	
	public static boolean isValidDateOfBirth (String dateOfBirth) {
		if (dateOfBirth.trim().isEmpty()) return true;
		try {
			LocalDate date = LocalDate.parse(dateOfBirth);
			if (!date.isBefore(LocalDate.now())) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidGender (String gender) {
		if (gender.trim().isEmpty()) return true;
		if (gender.equals("male") || gender.equals("female")) return true;
		return false;
	}
}
