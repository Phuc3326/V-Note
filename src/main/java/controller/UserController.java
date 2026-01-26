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
		}
	}
	
	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fullName = request.getParameter("fullName");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String passwordReEnter = request.getParameter("passwordReEnter");
		String email = request.getParameter("email");
		String dateOfBirth = request.getParameter("dateOfBirth");
		String phone = request.getParameter("phone");
		String gender = request.getParameter("gender");
		
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
				userDAO.insert(user);
				request.getSession().setAttribute("message", "Create account successfully.");
				response.sendRedirect(request.getContextPath() + "/user-controller?controllerType=sign-up");
			}
		}
	}
	
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
