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
			// Ho tro ca Heroku (DATABASE_URL) lan Docker Compose (DB_URL)
			String databaseUrl = System.getenv("DATABASE_URL");
			String url = System.getenv("DB_URL");
			String userName = System.getenv("DB_USER");
			String password = System.getenv("DB_PASS");
			
			if (databaseUrl != null && !databaseUrl.trim().isEmpty()) {
				try {
					java.net.URI dbUri = new java.net.URI(databaseUrl);
					String[] userInfo = dbUri.getUserInfo().split(":");
					userName = userInfo[0];
					password = userInfo[1];
					int port = (dbUri.getPort() != -1) ? dbUri.getPort() : 5432;
					url = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();
				} catch (Exception ex) {
					System.err.println("Failed to parse DATABASE_URL: " + ex.getMessage());
				}
			}
			
			if (url == null || url.trim().isEmpty()) {
				url = "jdbc:postgresql://vnote-db:5432/vnote_db";
			}
			
			if (userName == null || userName.trim().isEmpty()) {
				userName = "vnote_user";
			}
			
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
