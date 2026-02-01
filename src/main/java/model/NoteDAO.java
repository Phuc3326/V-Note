package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO implements DAOInterface<Note> {

	@Override
	public Note selectById(String idFind) {
		Note note = null;
		try {
			// Tao ket noi
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "SELECT * FROM notes WHERE id = ?;";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, idFind);
			
			// Thuc thi Statement
			ResultSet rs = st.executeQuery();
			
			// Xu li du lieu
			if (rs.next()) {
				note = new Note();
				note.setId(rs.getString("id"));
				
				User dummyUser = new User();
			    dummyUser.setId(rs.getString("userid"));
			    note.setUser(dummyUser);
				
				note.setTitle(rs.getString("title"));
				note.setContent(rs.getString("content"));
				note.setCreateDate(rs.getDate("createdate"));
				note.setLastEditDate(rs.getDate("lasteditdate"));
				note.setArchived(rs.getBoolean("isarchived"));
			}
			
			// Ngat ket noi database
			JDBCUtil.closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return note;
	}

	@Override
	public List<Note> selectAll() {
		List<Note> result = new ArrayList<>();
		
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "SELECT * FROM notes;";
			PreparedStatement st = c.prepareStatement(sql);
			
			// Thuc thi Statement
			ResultSet rs = st.executeQuery();
			
			// Xu li ket qua
			while (rs.next()) {
				Note note = new Note();
				note.setId(rs.getString("id"));
				
				User dummyUser = new User();
			    dummyUser.setId(rs.getString("userid"));
			    note.setUser(dummyUser);
				
				note.setTitle(rs.getString("title"));
				note.setContent(rs.getString("content"));
				note.setCreateDate(rs.getDate("createdate"));
				note.setLastEditDate(rs.getDate("lasteditdate"));
				note.setArchived(rs.getBoolean("isarchived"));
				result.add(note);
			}
			
			// Ngat ket noi database
			JDBCUtil.closeConnection(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean insert(Note t) {
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "INSERT INTO notes (id, userid, title, content, createdate, lasteditdate, isarchived) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, t.getId());
			st.setString(2, t.getUser().getId());
			st.setString(3, t.getTitle());
			st.setString(4, t.getContent());
			st.setDate(5, t.getCreateDate());
			st.setDate(6, t.getLastEditDate());
			st.setBoolean(7, t.isArchived());
			
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
	public boolean update(Note t) {
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "UPDATE notes "
					+ "SET userid = ?, title = ?, content = ?, createdate = ?, lasteditdate = ?, isarchived = ? "
					+ "WHERE id = ?;";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, t.getUser().getId());
			st.setString(2, t.getTitle());
			st.setString(3, t.getContent());
			st.setDate(4, t.getCreateDate());
			st.setDate(5, t.getLastEditDate());
			st.setBoolean(6, t.isArchived());
			st.setString(7, t.getId());
			
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
	public boolean delete(Note t) {
		try {
			// Tao Connection
			Connection c = JDBCUtil.getConnection();
			
			// Tao Statement
			String sql = "DELETE FROM notes "
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
	
	public List<Note> selectAllByUserId(String userId, boolean isArchived) {
	    List<Note> result = new ArrayList<>();
	    try {
	        Connection c = JDBCUtil.getConnection();
	        // Lọc theo cả UserId và trạng thái Thùng rác
	        String sql = "SELECT * FROM notes WHERE userid = ? AND isarchived = ? ORDER BY lasteditdate DESC;";
	        PreparedStatement st = c.prepareStatement(sql);
	        st.setString(1, userId);
	        st.setBoolean(2, isArchived);
	        
	        ResultSet rs = st.executeQuery();
	        while (rs.next()) {
	            Note note = new Note();
	            note.setId(rs.getString("id"));
	            
	            User dummyUser = new User();
	            dummyUser.setId(rs.getString("userid"));
	            note.setUser(dummyUser);
	            
	            note.setTitle(rs.getString("title"));
	            note.setContent(rs.getString("content"));
	            note.setCreateDate(rs.getDate("createdate"));
	            note.setLastEditDate(rs.getDate("lasteditdate"));
	            note.setArchived(rs.getBoolean("isarchived"));
	            result.add(note);
	        }
	        JDBCUtil.closeConnection(c);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	public List<Note> searchByTitle(String userId, String keyword) {
	    List<Note> result = new ArrayList<>();
	    try {
	        Connection c = JDBCUtil.getConnection();
	        // Sử dụng ILIKE (trong Postgres) hoặc LIKE để tìm kiếm gần đúng
	        String sql = "SELECT * FROM notes WHERE userid = ? AND isarchived = false AND title ILIKE ? ORDER BY lasteditdate DESC;";
	        PreparedStatement st = c.prepareStatement(sql);
	        st.setString(1, userId);
	        st.setString(2, "%" + keyword + "%"); // Tìm kiếm chuỗi chứa keyword
	        
	        ResultSet rs = st.executeQuery();
	        while (rs.next()) {
	            Note note = new Note();
	            note.setId(rs.getString("id"));
	            
	            User dummyUser = new User();
	            dummyUser.setId(rs.getString("userid"));
	            note.setUser(dummyUser);
	            
	            note.setTitle(rs.getString("title"));
	            note.setContent(rs.getString("content"));
	            note.setCreateDate(rs.getDate("createdate"));
	            note.setLastEditDate(rs.getDate("lasteditdate"));
	            note.setArchived(rs.getBoolean("isarchived"));
	            result.add(note);
	        }
	        JDBCUtil.closeConnection(c);
	    } catch (SQLException e) { e.printStackTrace(); }
	    return result;
	}
}
