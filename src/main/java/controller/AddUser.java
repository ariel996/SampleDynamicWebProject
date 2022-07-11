package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import model.Bhuser;
import service.DbUser;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String userName = request.getParameter("userName");
		String userEmail = request.getParameter("userEmail");
		String userPassword = request.getParameter("userPassword");
		String userMotto = request.getParameter("userMotto");
		String nextURL = "/error.jsp";
		
		Bhuser user = DbUser.getUserByEmail(userEmail);
		
		// create user and add them if they don't exit
		if (user == null) {
			user = new Bhuser();
			user.setUsername(userName);
			user.setUseremail(userEmail);
			user.setUserpassword(userPassword);
			Date joindate = Calendar.getInstance().getTime();
			user.setJoindate(joindate);
			user.setMotto(userMotto);
			
			DbUser.insert(user);
			nextURL = "/home.jsp";
		} else {
			String message = "You have an account -";
			request.setAttribute("message", message);
			nextURL = "/login.jsp";
		}
		
		session.setAttribute("user", user);
		getServletContext().getRequestDispatcher(nextURL).forward(request, response);
	}

}
