package org.servlet;

import java.sql.Connection;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beans.Product;
import org.utils.DBUtils;
import org.utils.MyUtils;

/**
 * Servlet implementation class DoCreateProductServlet
 */
@WebServlet("/doCreateProduct")
public class DoCreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DoCreateProductServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(request);

		String code = (String) request.getParameter("code");
		String name = (String) request.getParameter("name");
		String priceStr = (String) request.getParameter("price");

		float price = 0;
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Product product = new Product(code, name, price);
		String errorString = null;
		// ProductID is the string literal [a-zA-Z_0-9]
		// with at least 1 character
		String regex = "\\w+";
		if (code == null || !code.matches(regex)) {
			errorString = "Product Code invalid";
		}
		if (errorString == null) {
			try {
				DBUtils.insertProduct(conn, product);
			} catch (Exception e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}
		}
		// Store information to request attribute
		request.setAttribute("errorString", errorString);
		request.setAttribute("product", product);
		// If error, forward to Edit page.
		if (errorString != null) {
			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/productList");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
