package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAOInterface<User> {

	@Override
	public User selectById(String idFind) {
		User user = null;
		try {
			// Tao ket noi
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "SELECT * FROM users WHERE id = ?;";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, idFind);
			
			// Thuc thi Statement
			ResultSet rs = st.executeQuery();
			
			// Xu li du lieu
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setUserName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setDateOfBirth(rs.getDate("dateofbirth"));
				user.setGender(rs.getString("gender"));
				user.setFullName(rs.getString("fullname"));
				user.setPhone(rs.getString("phone"));
			}
			
			// Ngat ket noi database
			JDBCUtil.closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public User selectByUserName(String userNameFind) {
		User user = null;
		try {
			// Tao ket noi
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "SELECT * FROM users WHERE username = ?;";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, userNameFind);
			
			// Thuc thi Statement
			ResultSet rs = st.executeQuery();
			
			// Xu li du lieu
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setUserName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setDateOfBirth(rs.getDate("dateofbirth"));
				user.setGender(rs.getString("gender"));
				user.setFullName(rs.getString("fullname"));
				user.setPhone(rs.getString("phone"));
			}
			
			// Ngat ket noi database
			JDBCUtil.closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<User> selectAll() {
		List<User> result = new ArrayList<>();
		
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "SELECT * FROM users;";
			PreparedStatement st = c.prepareStatement(sql);
			
			// Thuc thi Statement
			ResultSet rs = st.executeQuery();
			
			// Xu li ket qua
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setUserName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setDateOfBirth(rs.getDate("dateofbirth"));
				user.setGender(rs.getString("gender"));
				user.setFullName(rs.getString("fullname"));
				user.setPhone(rs.getString("phone"));
				result.add(user);
			}
			
			// Ngat ket noi database
			JDBCUtil.closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean insert(User t) {
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "INSERT INTO users (id, username, password, email, dateofbirth, gender, fullname, phone) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, t.getId());
			st.setString(2, t.getUserName());
			st.setString(3, t.getPassword());
			st.setString(4, t.getEmail());
			st.setDate(5, t.getDateOfBirth());
			st.setString(6, t.getGender());
			st.setString(7, t.getFullName());
			st.setString(8, t.getPhone());
			
			// Thuc thi Statement
			int result = st.executeUpdate();
			
			// Dong ket noi
			JDBCUtil.closeConnection(c);
			
			// Xu li ket qua
			if (result > 0) {
	            System.out.println("Insert successfully!");
	            return true;
	        } else {
	            System.out.println("Insert failed: No rows affected.");
	            return false;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(User t) {
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "UPDATE users "
					+ "SET username = ?, password = ?, email = ?, dateofbirth = ?, gender = ?, fullname = ?, phone = ?"
					+ "WHERE id = ?;";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, t.getUserName());
			st.setString(2, t.getPassword());
			st.setString(3, t.getEmail());
			st.setDate(4, t.getDateOfBirth());
			st.setString(5, t.getGender());
			st.setString(6, t.getFullName());
			st.setString(7, t.getPhone());
			st.setString(8, t.getId());
			
			// Thuc thi Statement
			int result = st.executeUpdate();
			
			// Ngat ket noi
			JDBCUtil.closeConnection(c);
			
			// Xu li ket qua
			if (result > 0) {
				System.out.println("Update successfully!");
				return true;
			} else {
				System.out.println("Update failed! No row is affected.");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(User t) {
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "DELETE FROM users "
					+ "WHERE id = ?;";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, t.getId());
			
			// Thuc thi Statement
			int result = st.executeUpdate();
			
			// Ngat ket noi
			JDBCUtil.closeConnection(c);
			
			// Xu li ket qua
			if (result > 0) {
				System.out.println("Delete successfully!");
				return true;
			} else {
				System.out.println("Delete failed! No row is affected.");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
