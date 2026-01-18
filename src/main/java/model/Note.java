package model;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Note {
	private String id;
	private User user;
	private String title;
	private String content;
	private Date createDate;
	private Date lastEditDate;
	public Note() {
		this.id = generateId();
	}
	public Note(User user, String title, String content, Date createDate, Date lastEditDate) {
		this.id = generateId();
		this.user = user;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
		this.lastEditDate = lastEditDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastEditDate() {
		return lastEditDate;
	}
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}	
	
	private String generateId() {
		java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return "N" + sdf.format(now);
	}
}
