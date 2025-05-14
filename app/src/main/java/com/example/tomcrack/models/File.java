package com.example.tomcrack.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

public class File {
    private String id;
    private String name;
    private long size;
    private String path;
    private Category category; // can be null
    private Date uploadDate;
    @ServerTimestamp
    private Date createdAt;
    private String description;
    private User uploader; // can be null

    public File() {
        // Required empty constructor for Firestore deserialization
    }

    public File(String name, long size, String path, Category category, Date uploadDate,
                String description, User uploader) {
        this.name = name;
        this.size = size;
        this.path = path;
        this.category = category;
        this.uploadDate = uploadDate;
        this.description = description;
        this.uploader = uploader;
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public long getSize() {
        return size;
    }

    public void setSize(long size) { this.size = size; }

    public String getPath() {
        return path;
    }

    public void setPath(String path) { this.path = path; }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) { this.category = category; }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) { this.uploader = uploader; }
}
