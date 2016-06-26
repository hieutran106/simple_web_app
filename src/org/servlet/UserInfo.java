package org.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.beans.UserAccount;
import org.utils.MyUtils;

/**
 * Servlet implementation class UserInfo
 */
@WebServlet("/UserInfo")
public class UserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public UserInfo() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		
		//Check user has logged on
		UserAccount loginedUser=MyUtils.getLoginedUser(session);
		//Not logged in
		if (loginedUser==null) {
			//Redirect to login page
			response.sendRedirect(request.getContextPath()+"/login");
		} else {
			//Store info in request attribute
			request.setAttribute("user", loginedUser);
			//Forward to /WEB-INF/views/userInfoView.jsp
			RequestDispatcher dispatcher=this.getServletContext().getRequestDispatcher("/WEB-INF/views/userInfoView.jsp");
			dispatcher.forward(request, response);
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
