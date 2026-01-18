package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDAO;
import util.SecurityUtil;

import java.sql.Date;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/sign-up")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SignUp() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fullName = request.getParameter("fullName");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String dateOfBirthString = request.getParameter("dateOfBirth");
		String phone = request.getParameter("phone");
		String gender = request.getParameter("gender");
		
		String error = "";
		String url = "";
		
		UserDAO userDAO = new UserDAO();
		if (userDAO.selectByUserName(userName) != null) {
			error = "User name already exists!\n";
			request.setAttribute("error", error);
			url = "sign-up.jsp";
			request.getRequestDispatcher(url).forward(request, response);
		} else {
			String encryptedPassword = SecurityUtil.toSHA1(password);
			User user = new User(userName, encryptedPassword, email, Date.valueOf(dateOfBirthString), gender, fullName, phone);
			userDAO.insert(user);
			url = "sign-up-success.jsp";
			response.sendRedirect(url);
		}
		System.out.println(error);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
