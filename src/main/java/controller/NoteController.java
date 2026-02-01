package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.NoteDAO;
import model.User;
import model.Note;

/**
 * Servlet implementation class NoteController
 */
@WebServlet("/note-controller")
public class NoteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("controllerType");
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("user-controller?controllerType=login");
            return;
        }

        switch (action) {
            case "index":
                displayIndex(request, response, user);
                break;
            case "trash":
                displayTrash(request, response, user); // Hàm mới
                break;
            case "search":
            	search(request, response, user);
                break;
        }
	}
	
	

	private void search(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		NoteDAO noteDAO = new NoteDAO();
        List<Note> searchResults = noteDAO.searchByTitle(user.getId(), keyword);
        request.setAttribute("userNotes", searchResults);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("controllerType");
		
		User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("user-controller?controllerType=login");
            return;
        }

        switch (action) {
            case "save-note":
                saveNote(request, response, user);
                break;
            case "move-to-trash": // Thêm case này
                handleMoveToTrash(request, response, user);
                break;
            case "restore-note": // Case mới: Khôi phục
                handleRestoreNote(request, response, user);
                break;
            case "hard-delete": // Case mới: Xóa vĩnh viễn
                handleHardDelete(request, response, user);
                break;
        }
	}
	
	private void displayIndex(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        NoteDAO noteDAO = new NoteDAO();
        // Lấy danh sách note chưa bị xóa (isarchived = false)
        List<Note> userNotes = noteDAO.selectAllByUserId(user.getId(), false);
        request.setAttribute("userNotes", userNotes);
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
	
	private void displayTrash(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
	    NoteDAO noteDAO = new NoteDAO();
	    // TRUYỀN VÀO TRUE để lấy các note đã bị xóa tạm thời
	    List<Note> trashNotes = noteDAO.selectAllByUserId(user.getId(), true);
	    request.setAttribute("userNotes", trashNotes);
	    request.getRequestDispatcher("/WEB-INF/views/trash.jsp").forward(request, response);
	}
	
	private void saveNote(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
		try {
	        // 1. Lấy dữ liệu từ AJAX gửi về
	        String id = request.getParameter("id");
	        String title = request.getParameter("title");
	        String content = request.getParameter("content");

	        NoteDAO noteDAO = new NoteDAO();
	        boolean isSuccess = false;
	        
	        // Lấy thời gian hiện tại cho SQL
	        long currentTime = System.currentTimeMillis();
	        java.sql.Date sqlDate = new java.sql.Date(currentTime);

	        if (id == null || id.trim().isEmpty()) {
	            // TRƯỜNG HỢP: TẠO MỚI (INSERT)
	            // Constructor này sẽ tự gọi generateId() và set isArchived = false
	            Note newNote = new Note(user, title, content, sqlDate, sqlDate);
	            isSuccess = noteDAO.insert(newNote);
	        } else {
	            // TRƯỜNG HỢP: CẬP NHẬT (UPDATE)
	            // Lấy Note cũ từ DB lên để giữ lại ngày tạo (createDate)
	            Note existingNote = noteDAO.selectById(id);
	            
	            if (existingNote != null && existingNote.getUser().getId().equals(user.getId())) {
	                existingNote.setTitle(title);
	                existingNote.setContent(content);
	                existingNote.setLastEditDate(sqlDate); // Cập nhật ngày chỉnh sửa mới nhất
	                
	                isSuccess = noteDAO.update(existingNote);
	            }
	        }

	        // 3. Phản hồi cho AJAX (Phần này quyết định res.ok trong JavaScript)
	        if (isSuccess) {
	            response.setStatus(HttpServletResponse.SC_OK); // Trả về mã 200 - Thành công, res.ok trả về true
	        } else {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Trả về mã 500 - Thất bại, res.ok trả về false
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // res.ok trả về false
	    }
	}
	
	private void handleMoveToTrash(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
	    try {
	        String id = request.getParameter("id");
	        NoteDAO noteDAO = new NoteDAO();
	        
	        // 1. Lấy note từ DB lên để kiểm tra quyền sở hữu
	        Note note = noteDAO.selectById(id);
	        
	        if (note != null && note.getUser().getId().equals(user.getId())) {
	            // 2. Cập nhật trạng thái thành đã lưu trữ (isarchived = true)
	            note.setArchived(true);
	            
	            // 3. Gọi hàm update của DAO để lưu thay đổi vào Postgres
	            boolean isSuccess = noteDAO.update(note);
	            
	            if (isSuccess) {
	                response.setStatus(HttpServletResponse.SC_OK); // Trả về 200 cho AJAX reload trang
	            } else {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            }
	        } else {
	            // Không tìm thấy note hoặc không phải chủ sở hữu
	            response.sendError(HttpServletResponse.SC_FORBIDDEN);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
	}
	
	private void handleRestoreNote(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
	    try {
	        String id = request.getParameter("id");
	        NoteDAO noteDAO = new NoteDAO();
	        Note note = noteDAO.selectById(id);

	        // Kiểm tra quyền sở hữu trước khi thực hiện
	        if (note != null && note.getUser().getId().equals(user.getId())) {
	            note.setArchived(false); // Đưa về trạng thái hoạt động
	            boolean isSuccess = noteDAO.update(note);
	            
	            if (isSuccess) {
	                response.setStatus(HttpServletResponse.SC_OK);
	            } else {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            }
	        } else {
	            response.sendError(HttpServletResponse.SC_FORBIDDEN);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
	}
	
	private void handleHardDelete(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
	    try {
	        String id = request.getParameter("id");
	        NoteDAO noteDAO = new NoteDAO();
	        Note note = noteDAO.selectById(id);

	        // Bảo mật: Chỉ chủ sở hữu mới có quyền xóa vĩnh viễn
	        if (note != null && note.getUser().getId().equals(user.getId())) {
	            boolean isSuccess = noteDAO.delete(note); // Gọi hàm DELETE thực thụ
	            
	            if (isSuccess) {
	                response.setStatus(HttpServletResponse.SC_OK);
	            } else {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            }
	        } else {
	            response.sendError(HttpServletResponse.SC_FORBIDDEN);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
	}
}
