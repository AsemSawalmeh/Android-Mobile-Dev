package com.example.trackmaniaexchange;

import android.graphics.Bitmap;

public class trackRecord {
    private Bitmap thumbnail;
    private String authorName;
    private String title;

    public trackRecord(Bitmap thumbnail, String authorName, String title) {
        this.thumbnail = thumbnail;
        this.authorName = authorName;
        this.title = title;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "trackRecord{" +
                "thumbnail=" + thumbnail +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
