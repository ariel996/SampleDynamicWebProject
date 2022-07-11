package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Bhpost;
import service.DbPost;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class Newsfeed
 */
@WebServlet("/Newsfeed")
public class Newsfeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Newsfeed() {
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
		long filterByUserID = 0;
		String searchtext = "";
		String nextURL = "error.jsp";
		HttpSession session = request.getSession();
		if(session.getAttribute("user") == null) {
			nextURL = "login.jsp";
			session.invalidate();
			response.sendRedirect(request.getContextPath() + nextURL);
			return;
		}
		List<Bhpost> posts = null;
		if(request.getParameter("userid") != null && !request.getParameter("userid").isEmpty()) {
			filterByUserID = Integer.parseInt(request.getParameter("userid"));
			posts = DbPost.postsofUser(filterByUserID);
		} else if (request.getParameter("searchtext")!=null && !request.getParameter("searchtext").isEmpty()) {
			searchtext = request.getParameter("searchtext").toString();
			posts = DbPost.searchPosts(searchtext);
		} else {
			posts = DbPost.bhPost();
		}
		request.setAttribute("posts", posts);
		nextURL = "/newsfeed.jsp";
		getServletContext().getRequestDispatcher(nextURL).forward(request, response);
		
	}

}
