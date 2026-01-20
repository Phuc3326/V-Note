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
 * Servlet implementation class ChangePassword
 */
@WebServlet("/change-password")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Object obj = session.getAttribute("user");
		User user = (obj != null)?(User) obj: null;
		
		if (user == null) {
			response.sendRedirect("change-password.jsp");
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
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("change-password.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
