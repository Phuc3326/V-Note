package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	public static Connection getConnection() {
		Connection c = null;
		
		try {
			// Dang ky PostgreSQL Driver cho DriverManager quan li
			DriverManager.registerDriver(new org.postgresql.Driver());
			
			// Thuoc tinh de tao Connection
			String url = "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:5432/postgres";
			String userName = "postgres.xvwmeuwntggvyrxkfyrk";
			String password = "myfirstpersonalproject";
			
			// Tao Connection toi database V-Note su dung PostgreSQL Driver
			c = DriverManager.getConnection(url, userName, password);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}
	
	public static void closeConnection(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
