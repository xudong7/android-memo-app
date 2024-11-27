package com.example.memoapp.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memos")
public class Memo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content;
    private String imagePath;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}