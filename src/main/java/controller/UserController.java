package controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;
import util.CheckValidData;
import util.Email;
import util.SecurityUtil;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/user-controller")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("controllerType");
		
		if (type == null || type.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		
		switch (type) {	
		case "sign-up":
			request.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
			break;
		case "login":
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			break;
		case "change-information":
			request.getRequestDispatcher("/WEB-INF/views/change-information.jsp").forward(request, response);
			break;
		case "change-password":
			request.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(request, response);
			break;
		case "logout":
			logout(request, response);
			break;
		case "verify":
			request.getRequestDispatcher("/WEB-INF/views/verify.jsp").forward(request, response);
			break;
		case "resend-otp":
			resendOtp(request, response);
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("controllerType");
		
		if (type == null || type.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		
		switch (type) {	
		case "login":
			login(request, response);
			break;
		case "sign-up":
			signup(request, response);
			break;
		case "change-information":
			changeInformation(request, response);
			break;
		case "change-password":
			changePassword(request, response);
			break;
		case "logout":
			logout(request, response);
			break;
		case "verify":
			verify(request, response);
			break;
		}
	}
	
	private void resendOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
	    User pendingUser = (User) session.getAttribute("pendingUser");
		
	    if (pendingUser != null) {
	    	String newOtp = Email.generateOTP();
	    	session.setAttribute("sysOtp", newOtp);
	        session.setAttribute("otpTime", System.currentTimeMillis());
	        
	        String content = "<div style=\"font-family: Arial, sans-serif; text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 10px;\">" +
	                 "  <h2 style=\"color: #333;\">MÃ XÁC THỰC MỚI CỦA BẠN</h2>" +
	                 "  <p style=\"font-size: 18px; color: #555;\">Vui lòng sử dụng mã dưới đây để hoàn tất đăng ký:</p>" +
	                 "  <h1 style=\"font-size: 48px; color: #ffc107; letter-spacing: 10px; margin: 20px 0;\"><b>" + newOtp + "</b></h1>" +
	                 "  <p style=\"color: #888; font-size: 14px;\">Mã có hiệu lực trong vòng <b>60 giây</b>.</p>" +
	                 "  <hr style=\"border: 0; border-top: 1px solid #eee; margin: 20px 0;\">" +
	                 "  <p style=\"font-size: 12px; color: #aaa;\">Đây là email tự động, vui lòng không phản hồi.</p>" +
	                 "</div>";
	        
	        Email.sendEmail(pendingUser.getEmail(), content);
	        session.setAttribute("msg", "Mã xác thực mới đã được gửi!");
	    }
	    response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=verify");
	}
	
	private void verify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userOtp = request.getParameter("otpInput");
		HttpSession session = request.getSession();
		String systemOtp = (String) session.getAttribute("sysOtp");
		Long otpTime = (Long) session.getAttribute("otpTime");
		User pendingUser = (User) session.getAttribute("pendingUser");
    	
    	if (pendingUser == null) {
	        response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=sign-up");
	        return;
	    }

		long currentTime = System.currentTimeMillis();

		if (systemOtp != null && systemOtp.equals(userOtp)) {
		    if (currentTime - otpTime <= 60000) { // 60 giây = 60,000 ms
		        // THÀNH CÔNG: Lưu user vào Database tại đây
		    	UserDAO userDAO = new UserDAO();    	
		    	userDAO.insert(pendingUser);
		        session.removeAttribute("sysOtp");
		        session.removeAttribute("otpTime");
		        session.removeAttribute("pendingUser");
		        session.setAttribute("msg", "Xác thực thành công! Bạn có thể đăng nhập ngay.");
		        response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=verify");
		        // Chuyển sang trang thông báo thành công có nút về Home
		    } else {
		    	session.setAttribute("msg", "Mã xác thực đã hết hạn.");
		        response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=verify");
		    }
		} else {
			session.setAttribute("msg", "Xác thực không đúng");
	        response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=verify");
		}
		
	}

	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fullName = request.getParameter("fullName");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String passwordReEnter = request.getParameter("passwordReEnter");
		String email = request.getParameter("email");
		String dateOfBirth = request.getParameter("dateOfBirth");
		dateOfBirth = (dateOfBirth != null)? dateOfBirth : "";
		String phone = request.getParameter("phone");
		phone = (phone != null)? phone : "";
		String gender = request.getParameter("gender");
		gender = (gender != null)? gender : "";
		
		String msg = "";
		if (!CheckValidData.isValidFullName(fullName)) {
			msg += "Full name can not be empty!\n";
		}
		if (!CheckValidData.isValidUserName(userName)) {
			msg += "User name can not be empty!\n";
		}
		if (!CheckValidData.isValidPassword(password)) {
			msg += "Password must contain at least 8 character!\n";
		}
		else if (!CheckValidData.isValidReEnterPassword(password, passwordReEnter)) {
			msg += "Re-enter password must match the password!\n";
		}
		if (!CheckValidData.isValidPhone(phone)) {
			msg += "Number phone's format invalid!\n";
		}
		if (!CheckValidData.isValidEmail(email)) {
			msg += "email's format invalid!\n";
		}
		if (!CheckValidData.isValidDateOfBirth(dateOfBirth)) {
			msg += "Date of birth's format invalid!\n";
		}
		if (!CheckValidData.isValidGender(gender)) {
			msg += "Gender's format invalid!\n";
		}
		
		if (!msg.isEmpty()) {
			request.setAttribute("message", msg);
			request.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
		} else {
			Date date_of_birth = (dateOfBirth.isEmpty()) ? null : Date.valueOf(dateOfBirth);
			UserDAO userDAO = new UserDAO();
			if (userDAO.selectByUserName(userName) != null) {
				msg = "User name already exists!\n";
				request.setAttribute("message", msg);
				request.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
			} else {
				String encryptedPassword = SecurityUtil.toSHA1(password);
				User user = new User(userName, encryptedPassword, email, date_of_birth, gender, fullName, phone);
//				userDAO.insert(user);
//				request.getSession().setAttribute("message", "Create account successfully.");
//				response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=sign-up");
				
				HttpSession session = request.getSession();
				String sysOtp = Email.generateOTP();
				session.setAttribute("sysOtp", sysOtp);
				session.setAttribute("otpTime", System.currentTimeMillis());
				session.setAttribute("pendingUser", user);
				
				String content = "<div style=\"font-family: Arial, sans-serif; text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 10px;\">" +
		                 "  <h2 style=\"color: #333;\">MÃ XÁC THỰC CỦA BẠN</h2>" +
		                 "  <p style=\"font-size: 18px; color: #555;\">Vui lòng sử dụng mã dưới đây để hoàn tất đăng ký:</p>" +
		                 "  <h1 style=\"font-size: 48px; color: #ffc107; letter-spacing: 10px; margin: 20px 0;\"><b>" + sysOtp + "</b></h1>" +
		                 "  <p style=\"color: #888; font-size: 14px;\">Mã có hiệu lực trong vòng <b>60 giây</b>.</p>" +
		                 "  <hr style=\"border: 0; border-top: 1px solid #eee; margin: 20px 0;\">" +
		                 "  <p style=\"font-size: 12px; color: #aaa;\">Đây là email tự động, vui lòng không phản hồi.</p>" +
		                 "</div>";
				
				Email.sendEmail(user.getEmail(), content);
				response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=verify");
			}
		}
	}
	
//	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String fullName = request.getParameter("fullName");
//		String userName = request.getParameter("userName");
//		String password = request.getParameter("password");
//		String passwordReEnter = request.getParameter("passwordReEnter");
//		String email = request.getParameter("email");
//		String dateOfBirth = request.getParameter("dateOfBirth");
//		String phone = request.getParameter("phone");
//		String gender = request.getParameter("gender");
//		
//		String msg = "";
//		if (!CheckValidData.isValidFullName(fullName)) {
//			msg += "Full name can not be empty!\n";
//		}
//		if (!CheckValidData.isValidUserName(userName)) {
//			msg += "User name can not be empty!\n";
//		}
//		if (!CheckValidData.isValidPassword(password)) {
//			msg += "Password must contain at least 8 character!\n";
//		}
//		else if (!CheckValidData.isValidReEnterPassword(password, passwordReEnter)) {
//			msg += "Re-enter password must match the password!\n";
//		}
//		if (!CheckValidData.isValidPhone(phone)) {
//			msg += "Number phone's format invalid!\n";
//		}
//		if (!CheckValidData.isValidEmail(email)) {
//			msg += "email's format invalid!\n";
//		}
//		if (!CheckValidData.isValidDateOfBirth(dateOfBirth)) {
//			msg += "Date of birth's format invalid!\n";
//		}
//		if (!CheckValidData.isValidGender(gender)) {
//			msg += "Gender's format invalid!\n";
//		}
//		
//		if (!msg.isEmpty()) {
//			request.setAttribute("message", msg);
//			request.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
//		} else {
//			Date date_of_birth = (dateOfBirth.isEmpty()) ? null : Date.valueOf(dateOfBirth);
//			UserDAO userDAO = new UserDAO();
//			if (userDAO.selectByUserName(userName) != null) {
//				msg = "User name already exists!\n";
//				request.setAttribute("message", msg);
//				request.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
//			} else {
//				String encryptedPassword = SecurityUtil.toSHA1(password);
//				User user = new User(userName, encryptedPassword, email, date_of_birth, gender, fullName, phone);
//				userDAO.insert(user);
//				request.getSession().setAttribute("message", "Create account successfully.");
//				response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=sign-up");
//			}
//		}
//	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessionCheck = request.getSession();
		Object objSes = sessionCheck.getAttribute("user");
		User user = (objSes != null) ? (User) objSes : null;
		if (user == null) {
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			
			UserDAO userDAO = new UserDAO();
			user = userDAO.selectByUserName(userName);
			String encryptedPassword = SecurityUtil.toSHA1(password);
			if (user != null && user.getPassword().equals(encryptedPassword)) {
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect(request.getContextPath() + "/");
			} else {
				request.setAttribute("message", "Account does not exist!");
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
	}
	
	private void changeInformation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object objSes = session.getAttribute("user");
		User user = (objSes != null) ? (User) objSes : null;
		
		if (user == null) {
			request.getRequestDispatcher("/WEB-INF/views/change-information.jsp").forward(request, response);
		} else {
			String fullName = request.getParameter("fullName");
			String email = request.getParameter("email");
			Date dateOfBirth = (request.getParameter("dateOfBirth").equals(""))?null:Date.valueOf(request.getParameter("dateOfBirth"));
			String phone = request.getParameter("phone");
			String gender = request.getParameter("gender");
			
			UserDAO userDAO = new UserDAO();
			user = new User(user.getId(), user.getUserName(), user.getPassword(), email, dateOfBirth, gender, fullName, phone);
			userDAO.update(user);
			session.setAttribute("user", user);
			System.out.println("Save new information successfully!");
			session.setAttribute("msg", "Save successfully!");
			response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=change-information");
		}
	}

	private void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		User user = (obj != null)?(User) obj: null;
		
		if (user == null) {
			request.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(request, response);
		} else {
			String password = request.getParameter("password");
			String newPassword = request.getParameter("newPassword");
			
			String encryptedPassword = SecurityUtil.toSHA1(password);
			String msg = "";
			if (encryptedPassword.equals(user.getPassword())) {
				String encryptedNewPassword = SecurityUtil.toSHA1(newPassword);
				user.setPassword(encryptedNewPassword);
				UserDAO userDAO = new UserDAO();
				userDAO.update(user);
				System.out.println("Save new password successfully!");
				msg = "Save successfully!";
			} else {
				msg ="Wrong current password!";
			}
			session.setAttribute("msg", msg);
			response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=change-password");
		}
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Tham so false nghia la khong tao moi mot Session khi dang khong ton tai Session va tra ve null
		HttpSession session = request.getSession(false); 
		
		if (session != null) {
			session.invalidate();
		}
		
		response.sendRedirect(request.getContextPath() + "/");
	}
}
