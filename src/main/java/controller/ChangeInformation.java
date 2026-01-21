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

/**
 * Servlet implementation class ChangeInformation
 */
@WebServlet("/change-information")
public class ChangeInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeInformation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("change-information.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Object objSes = session.getAttribute("user");
		User user = (objSes != null) ? (User) objSes : null;
		
		if (user == null) {
			request.getRequestDispatcher("change-information.jsp").forward(request, response);
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
			String msg = "Save successfully!";
			System.out.println("Save new information successfully!");
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("change-information.jsp").forward(request, response);
		}
	}

}
