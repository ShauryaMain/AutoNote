package com.lecturenotes.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Note {

    private final String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

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

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    private void touch() {
        modifiedAt = LocalDateTime.now();
    }
}
