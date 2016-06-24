package org.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.beans.UserAccount;
import org.utils.DBUtils;
import org.utils.MyUtils;

@WebServlet(urlPatterns={"/doLogin"})
public class DoLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
    public DoLoginServlet() {
        super();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userName=req.getParameter("userName");
    	String password=req.getParameter("password");
    	String rememberMeStr=req.getParameter("rememberMe");
    	boolean remember="Y".equals(rememberMeStr);
    	
    	UserAccount user=null;
    	boolean hasError=false;
    	String errorString=null;
    	
    	if (userName==null || password==null || userName.length()==0 || password.length()==0) {
    		hasError=true;
    		errorString = "Required username and password";
    	} else {
    		Connection conn=MyUtils.getStoredConnection(req);
    		try {
    			user=DBUtils.findUser(conn, userName, password);
    			if (user==null) {
    				hasError=true;
    				errorString="UserName or password invalid";
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    			hasError=true;
    			errorString=e.getMessage();
    		}
    	}
    	
    	//If error, forward to /WEB-INF/views/login.jsp
    	if (hasError) {
    		user =new UserAccount();
    		user.setUserName(userName);
    		user.setPassword(password);
    		
    		//Store information in request attribute, before forward
    		req.setAttribute("errorString", errorString);
            req.setAttribute("user", user);
            
            // Forward to /WEB-INF/views/login.jsp
            RequestDispatcher dispatcher //
            = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
 
            dispatcher.forward(req, resp);
    	} else {
    		//If no error
        	//Store information in Session
        	// And redirect to userInfo page
    		HttpSession session=req.getSession();
    		MyUtils.storeLoginedUser(session, user);
    		
    		//If user checked "Remember Me"
    		if (remember) {
    			MyUtils.storedUserCookie(resp, user);
    		} else {
    			//Delete cookie
    		}
    		// Redirect to userInfo page.
            resp.sendRedirect(req.getContextPath() + "/userInfo");
    	}
    	
    	
    	
    }
}
