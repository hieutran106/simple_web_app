package org.utils;

import java.sql.Connection;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.parser.HttpParser;
import org.beans.UserAccount;

public class MyUtils {
	public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";
	private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

	// Store Connection in request attribute
	// (Information stored only exist during request
	public static void storeConnection(ServletRequest req, Connection conn) {
		req.setAttribute(ATT_NAME_CONNECTION, conn);
	}

	// Get Connection object
	public static Connection getStoredConnection(ServletRequest req) {
		Connection conn = (Connection) req.getAttribute(ATT_NAME_CONNECTION);
		return conn;
	}

	// Store user info in Session
	public static void storeLoginedUser(HttpSession session, UserAccount loginUser) {
		// On the JSP can access ${loginedUser}
		session.setAttribute("loginedUser", loginUser);
	}

	// Get the user information stored in the session
	public static UserAccount getLoginedUser(HttpSession session) {
		return (UserAccount) session.getAttribute("loginedUser");
	}

	// Store info in Cookie
	public static void storedUserCookie(HttpServletResponse response, UserAccount user) {
		System.out.println("Store user cookie");
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
		// 1 day (Convert to seconds)
		cookieUserName.setMaxAge(24 * 60 * 60);
		response.addCookie(cookieUserName);
	}

	public static String getUserNameInCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	// Delete cookie.
	public static void deleteUserCookie(HttpServletResponse response) {
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);

		// 0 seconds (Expires immediately)
		cookieUserName.setMaxAge(0);
		response.addCookie(cookieUserName);
	}
}
