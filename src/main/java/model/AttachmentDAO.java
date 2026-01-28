package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO implements DAOInterface<Attachment> {

    @Override
    public boolean insert(Attachment t) {
        try {
            Connection c = JDBCUtil.getConnection();
            String sql = "INSERT INTO attachments (id, noteid, filename, fileid, filetype) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, t.getId());
            st.setString(2, t.getNote().getId());
            st.setString(3, t.getFileName());
            st.setString(4, t.getFileId());
            st.setString(5, t.getFileType());
            
            int result = st.executeUpdate();
            JDBCUtil.closeConnection(c);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm quan trọng nhất để hiển thị file trong Note
    public List<Attachment> selectByNoteId(String noteId) {
        List<Attachment> list = new ArrayList<>();
        NoteDAO noteDAO = new NoteDAO();
        try {
            Connection c = JDBCUtil.getConnection();
            String sql = "SELECT * FROM attachments WHERE noteid = ?";
            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, noteId);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                Attachment att = new Attachment(
                    rs.getString("id"),
                    noteDAO.selectById(rs.getString("noteid")),
                    rs.getString("filename"),
                    rs.getString("fileid"),
                    rs.getString("filetype")
                );
                list.add(att);
            }
            JDBCUtil.closeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean delete(Attachment t) {
        try {
            Connection c = JDBCUtil.getConnection();
            String sql = "DELETE FROM attachments WHERE id = ?";
            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, t.getId());
            int result = st.executeUpdate();
            JDBCUtil.closeConnection(c);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Attachment selectById(String id) { return null; }

    @Override
    public List<Attachment> selectAll() { return new ArrayList<>(); }

    @Override
    public boolean update(Attachment t) { return false; }
}