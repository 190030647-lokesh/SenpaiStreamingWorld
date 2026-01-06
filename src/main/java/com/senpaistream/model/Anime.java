package com.senpaistream.model;

import java.sql.Timestamp;

public class Anime {
    private int animeId;
    private String title;
    private String description;
    private String genre;
    private int releaseYear;
    private int totalEpisodes;
    private String posterUrl;
    private double rating;
    private Timestamp createdAt;
    
    public Anime() {}
    
    public Anime(int animeId, String title, String description, String genre, 
                 int releaseYear, int totalEpisodes, String posterUrl, double rating) {
        this.animeId = animeId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.totalEpisodes = totalEpisodes;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }
    
    // Getters and Setters
    public int getAnimeId() {
        return animeId;
    }
    
    public void setAnimeId(int animeId) {
        this.animeId = animeId;
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
    
    public int getReleaseYear() {
        return releaseYear;
    }
    
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
    
    public int getTotalEpisodes() {
        return totalEpisodes;
    }
    
    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }
    
    public String getPosterUrl() {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
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
