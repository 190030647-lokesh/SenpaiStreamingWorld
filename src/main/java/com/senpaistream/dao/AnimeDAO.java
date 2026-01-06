package com.senpaistream.dao;

import com.senpaistream.model.Anime;
import com.senpaistream.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimeDAO {
    
    // Get all anime
    public List<Anime> getAllAnime() {
        List<Anime> animeList = new ArrayList<>();
        String sql = "SELECT * FROM anime ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Anime anime = extractAnimeFromResultSet(rs);
                animeList.add(anime);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return animeList;
    }
    
    // Get anime by genre
    public List<Anime> getAnimeByGenre(String genre) {
        List<Anime> animeList = new ArrayList<>();
        String sql = "SELECT * FROM anime WHERE genre LIKE ? ORDER BY rating DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + genre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Anime anime = extractAnimeFromResultSet(rs);
                animeList.add(anime);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return animeList;
    }
    
    // Get anime by ID
    public Anime getAnimeById(int animeId) {
        String sql = "SELECT * FROM anime WHERE anime_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, animeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAnimeFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Search anime by title
    public List<Anime> searchAnime(String keyword) {
        List<Anime> animeList = new ArrayList<>();
        String sql = "SELECT * FROM anime WHERE title LIKE ? ORDER BY rating DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Anime anime = extractAnimeFromResultSet(rs);
                animeList.add(anime);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return animeList;
    }
    
    // Get continue watching anime for user
    public List<Anime> getContinueWatching(int userId) {
        List<Anime> animeList = new ArrayList<>();
        String sql = "SELECT DISTINCT a.* FROM anime a " +
                    "INNER JOIN watch_history wh ON a.anime_id = wh.anime_id " +
                    "WHERE wh.user_id = ? AND wh.status = 'watching' " +
                    "ORDER BY wh.last_watched DESC LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Anime anime = extractAnimeFromResultSet(rs);
                animeList.add(anime);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return animeList;
    }
    
    // Get completed anime for user
    public List<Anime> getCompletedAnime(int userId) {
        List<Anime> animeList = new ArrayList<>();
        String sql = "SELECT DISTINCT a.* FROM anime a " +
                    "INNER JOIN watch_history wh ON a.anime_id = wh.anime_id " +
                    "WHERE wh.user_id = ? AND wh.status = 'completed' " +
                    "ORDER BY wh.last_watched DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Anime anime = extractAnimeFromResultSet(rs);
                animeList.add(anime);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return animeList;
    }
    
    // Helper method to extract Anime from ResultSet
    private Anime extractAnimeFromResultSet(ResultSet rs) throws SQLException {
        Anime anime = new Anime();
        anime.setAnimeId(rs.getInt("anime_id"));
        anime.setTitle(rs.getString("title"));
        anime.setDescription(rs.getString("description"));
        anime.setGenre(rs.getString("genre"));
        anime.setReleaseYear(rs.getInt("release_year"));
        anime.setTotalEpisodes(rs.getInt("total_episodes"));
        anime.setPosterUrl(rs.getString("poster_url"));
        anime.setRating(rs.getDouble("rating"));
        anime.setCreatedAt(rs.getTimestamp("created_at"));
        return anime;
    }
}
