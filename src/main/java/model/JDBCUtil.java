package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	public static Connection getConnection() {
		Connection c = null;
		
		try {
			// Dang ky PostgreSQL Driver cho DriverManager quan ly
			DriverManager.registerDriver(new org.postgresql.Driver());
			
			// Doc cac thong tin ket noi tu bien moi truong
			// Neu khong truyen DB_URL, mac dinh tro ve service database 'vnote-db' cua Docker
			String url = System.getenv("DB_URL");
			if (url == null || url.trim().isEmpty()) {
				url = "jdbc:postgresql://vnote-db:5432/vnote_db";
			}
			
			String userName = System.getenv("DB_USER");
			if (userName == null || userName.trim().isEmpty()) {
				userName = "vnote_user";
			}
			
			String password = System.getenv("DB_PASS");
			if (password == null || password.trim().isEmpty()) {
				password = "vnote_secret_pass";
			}
			
			// Tao Connection toi PostgreSQL
			c = DriverManager.getConnection(url, userName, password);
			
		} catch (SQLException e) {
			System.err.println("Database Connection Failed: " + e.getMessage());
			e.printStackTrace();
		}
		
		return c;
	}
	
	public static void closeConnection(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
