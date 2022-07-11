package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import service.DbUser;
import model.Bhuser;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String useremail = request.getParameter("email");
		String userpassword = request.getParameter("password");
		String action = request.getParameter("action");
		String nextURL = "/error.jsp";
		
		HttpSession session = request.getSession();
		if (action.equals("logout")) {
			session.invalidate();
			nextURL = "/login.jsp";
		} else {
			if (DbUser.isValidUser(useremail, userpassword)) {
				Bhuser user = DbUser.getUserByEmail(useremail);
				session.setAttribute("user", user);
				int gravatarImageWidth = 30;
				
				String gravatarURL = DbUser.getGravatarURL(useremail, gravatarImageWidth);
				session.setAttribute("gravatarURL", gravatarURL);
				nextURL = "/home.jsp";
			} else {
				nextURL = "/login.jsp";
			}
		}
		getServletContext().getRequestDispatcher(nextURL).forward(request, response);
	}

}
