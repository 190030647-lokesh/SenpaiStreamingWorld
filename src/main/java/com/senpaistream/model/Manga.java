package com.senpaistream.model;

import java.sql.Timestamp;

public class Manga {
    private int mangaId;
    private String title;
    private String description;
    private String genre;
    private String author;
    private int totalChapters;
    private String coverUrl;
    private double rating;
    private Timestamp createdAt;
    
    public Manga() {}
    
    public Manga(int mangaId, String title, String description, String genre, 
                 String author, int totalChapters, String coverUrl, double rating) {
        this.mangaId = mangaId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.author = author;
        this.totalChapters = totalChapters;
        this.coverUrl = coverUrl;
        this.rating = rating;
    }
    
    // Getters and Setters
    public int getMangaId() {
        return mangaId;
    }
    
    public void setMangaId(int mangaId) {
        this.mangaId = mangaId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public int getTotalChapters() {
        return totalChapters;
    }
    
    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }
    
    public String getCoverUrl() {
        return coverUrl;
    }
    
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
