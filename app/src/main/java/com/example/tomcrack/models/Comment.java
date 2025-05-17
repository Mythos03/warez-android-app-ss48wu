package com.example.tomcrack.models;

import java.util.Date;

public class Comment {
    private String id;
    private String content;
    private User author;
    private File file;
    private Date createdAt;

    public Comment() {}

    public Comment(String id, String content, User author, File file, Date createdAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.file = file;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public File getFile() {
        return file;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
