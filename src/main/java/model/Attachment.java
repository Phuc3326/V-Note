package model;

import java.util.UUID;

public class Attachment {
    private String id;
    private Note note;      // Gắn với ghi chú nào
    private String fileName; // Tên gốc (VD: btap.pdf)
    private String fileId;   // Tên lưu trên ổ cứng (VD: UUID.pdf)
    private String fileType; // Loại file (MIME type)

    public Attachment() {
        this.id = "A" + System.currentTimeMillis();
        this.fileId = UUID.randomUUID().toString();
    }

    // Constructor dùng để nhận file mới upload
    public Attachment(Note note, String fileName, String fileType) {
        this();
        this.note = note;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    // Constructor đầy đủ cho DAO mapping
    public Attachment(String id, Note note, String fileName, String fileId, String fileType) {
        this.id = id;
        this.note = note;
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileType = fileType;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Note getNote() { return note; }
    public void setNote(Note note) { this.note = note; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}