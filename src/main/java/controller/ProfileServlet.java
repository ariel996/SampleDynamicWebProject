package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;

import service.DbUser;
import model.Bhuser;

/**
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String nextURL = "/error.jsp";
		long userid = 0;
		String action = "";
		
		Bhuser profileUser = null;
		Bhuser loggedInUser = null;
		
		if (session.getAttribute("user") == null) {
			nextURL = "/login.jsp";
			session.invalidate();
			response.sendRedirect(request.getContextPath() + nextURL);
			return;
		}
		
		try {
			userid = Long.parseLong(request.getParameter("userid"));
			action = request.getParameter("action");
			if (request.getParameter("action").equals("updateprofile")) {
				long uid = Long.parseLong(request.getParameter("userid"));
				String userEmail = request.getParameter("useremail");
				String userMotto = request.getParameter("usermotto");
				
				Bhuser updateUser = DbUser.getUser(uid);
				updateUser.setMotto(userMotto);
				updateUser.setUseremail(userEmail);
				
				DbUser.update(updateUser);
			}
			
			// get the user from the parameter
			profileUser = DbUser.getUser(userid);
			// get the current user out of the session
			loggedInUser = (Bhuser) session.getAttribute("user");
			
			if (profileUser.getBhuserid()==loggedInUser.getBhuserid()) {
				session.setAttribute("editProfile", true);
			} else {
				session.setAttribute("editProfile", false);
			}
			// populate the data in the attributes
			int imgSize = 120;
			SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
			String joindate = sdf.format(profileUser.getJoindate());
			
			request.setAttribute("userid", profileUser.getBhuserid());
			request.setAttribute("userimage", DbUser.getGravatarURL(profileUser.getUseremail(), imgSize));
			request.setAttribute("username", profileUser.getUsername());
			request.setAttribute("useremail", profileUser.getUseremail());
			request.setAttribute("userjoindate", joindate);
			nextURL = "/profile.jsp";
		} catch (Exception e) {
			System.out.println(e);
		}
		getServletContext().getRequestDispatcher(nextURL).forward(request, response);
	}

}
