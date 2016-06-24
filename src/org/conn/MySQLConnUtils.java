package org.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnUtils {
	public static Connection getMySQLConnection() throws ClassNotFoundException, SQLException {
		String hostName="localhost";
		String dbName="simple_app";
		String userName="root";
		String password="123456a@";
		return getMySQLConnection(hostName, dbName, userName, password);
		
	}
	
	 
	public static Connection getMySQLConnection(String hostName, String dbName,
	        String userName, String password) throws SQLException,
	        ClassNotFoundException {
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String connectionURL="jdbc:mysql://"+hostName+":3306/"+dbName;
		Connection conn=DriverManager.getConnection(connectionURL, userName, password);
		return conn;
	}
	
}
