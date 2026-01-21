package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;
import util.SecurityUtil;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessionCheck = request.getSession(false);
		Object objSes = sessionCheck.getAttribute("user");
		User user = (objSes != null) ? (User) objSes : null;
		if (user == null) {
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			
			String error = "";
			String url = "";
			UserDAO userDAO = new UserDAO();
			user = userDAO.selectByUserName(userName);
			String encryptedPassword = SecurityUtil.toSHA1(password);
			if (user != null && user.getPassword().equals(encryptedPassword)) {
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				url = "index.jsp";
			} else {
				error = "The account does not exist!";
				request.setAttribute("error", error);
				url = "login.jsp";
			}
			request.getRequestDispatcher(url).forward(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}

}
