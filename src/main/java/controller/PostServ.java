package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Bhpost;
import model.Bhuser;
import service.DbPost;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Servlet implementation class PostServ
 */
@WebServlet("/PostServ")
public class PostServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostServ() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String posttext = request.getParameter("posttext");
		String nextURL = "/error.jsp";
		HttpSession session = request.getSession();
		if (session.getAttribute("user")==null) {
			nextURL = "/login.jsp";
			session.invalidate();
		} else {
			Bhuser bhuser = (Bhuser)session.getAttribute("user");
			
			// insert the post
			Bhpost bhpost = new Bhpost();
			bhpost.setBhuser(bhuser);
			Date postdate = Calendar.getInstance().getTime();
			bhpost.setPostdate(postdate);
			bhpost.setPosttext(posttext);
			DbPost.insert(bhpost);
			nextURL = "/Newsfeed";
		}
		getServletContext().getRequestDispatcher(nextURL).forward(request, response);
	}

}
