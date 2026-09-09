package com.lecturenotes.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Note {

    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String folderId;

    public Note() {
        // Required for JSON deserialization
    }

    public Note(String title, String content) {

        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;

        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        touch();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        touch();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    private void touch() {
        modifiedAt = LocalDateTime.now();
    }

    public String getFolderId() {
      return folderId;
    }

    public void setFolderId() {
      this.folderId = folderId;
    }
}

