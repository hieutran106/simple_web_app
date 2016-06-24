package org.filter;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.conn.ConnectionUtils;
import org.utils.MyUtils;

@WebFilter(filterName = "jdbcFilter", urlPatterns = { "/*" })
public class JDBCFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		//Only open connections for the special request need connection
		//Avoid open connection for commons request 
		//For example (image, css, javascript..)
		if (this.needJDBC(req)) {
			System.out.println("Open Connection for"+req.getServletPath());
			Connection conn=null;
			try {
				conn=ConnectionUtils.getConnection();
				conn.setAutoCommit(false);
				MyUtils.storeConnection(request, conn);
				
				//Allow request to go forward
				chain.doFilter(request, response);
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				ConnectionUtils.rollBackQuietly(conn);
				throw new ServletException();
			} finally {
				ConnectionUtils.closeQuietly(conn);
			}
		} else {
			// With commons requests (images, css, html, ..)
		       // No need to open the connection.     
			 chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
	
	//Check the target of the request is s servlet
	private boolean needJDBC(HttpServletRequest req) {
		System.out.print("JDBS Filter");
		//Servlet Url-pattern: /spath/*
		//=> /spath
		String servletPath=req.getServletPath();
		String pathInfo=req.getPathInfo();
		String urlPattern=servletPath;
		
		if (pathInfo!=null) {
			urlPattern=servletPath+"/*";
		}
		
		//Key: servletName
		//Value: ServletRegistration
		Map<String,? extends ServletRegistration> servletRegistrations=req.getServletContext().getServletRegistrations();
		
		//Collection of all servlet in your webapp
		Collection <? extends ServletRegistration> values=servletRegistrations.values();
		for(ServletRegistration sr: values) {
			Collection<String> mappings=sr.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}

}
