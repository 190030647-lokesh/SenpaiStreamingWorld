package com.senpaistream.dao;

import com.senpaistream.model.Manga;
import com.senpaistream.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MangaDAO {
    
    // Get all manga
    public List<Manga> getAllManga() {
        List<Manga> mangaList = new ArrayList<>();
        String sql = "SELECT * FROM manga ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Manga manga = extractMangaFromResultSet(rs);
                mangaList.add(manga);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mangaList;
    }
    
    // Get manga by genre
    public List<Manga> getMangaByGenre(String genre) {
        List<Manga> mangaList = new ArrayList<>();
        String sql = "SELECT * FROM manga WHERE genre LIKE ? ORDER BY rating DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + genre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Manga manga = extractMangaFromResultSet(rs);
                mangaList.add(manga);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mangaList;
    }
    
    // Get manga by ID
    public Manga getMangaById(int mangaId) {
        String sql = "SELECT * FROM manga WHERE manga_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, mangaId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractMangaFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Search manga by title
    public List<Manga> searchManga(String keyword) {
        List<Manga> mangaList = new ArrayList<>();
        String sql = "SELECT * FROM manga WHERE title LIKE ? OR author LIKE ? ORDER BY rating DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Manga manga = extractMangaFromResultSet(rs);
                mangaList.add(manga);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mangaList;
    }
    
    // Get continue reading manga for user
    public List<Manga> getContinueReading(int userId) {
        List<Manga> mangaList = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM manga m " +
                    "INNER JOIN read_history rh ON m.manga_id = rh.manga_id " +
                    "WHERE rh.user_id = ? AND rh.status = 'reading' " +
                    "ORDER BY rh.last_read DESC LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Manga manga = extractMangaFromResultSet(rs);
                mangaList.add(manga);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mangaList;
    }
    
    // Get completed manga for user
    public List<Manga> getCompletedManga(int userId) {
        List<Manga> mangaList = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM manga m " +
                    "INNER JOIN read_history rh ON m.manga_id = rh.manga_id " +
                    "WHERE rh.user_id = ? AND rh.status = 'completed' " +
                    "ORDER BY rh.last_read DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Manga manga = extractMangaFromResultSet(rs);
                mangaList.add(manga);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mangaList;
    }
    
    // Helper method to extract Manga from ResultSet
    private Manga extractMangaFromResultSet(ResultSet rs) throws SQLException {
        Manga manga = new Manga();
        manga.setMangaId(rs.getInt("manga_id"));
        manga.setTitle(rs.getString("title"));
        manga.setDescription(rs.getString("description"));
        manga.setGenre(rs.getString("genre"));
        manga.setAuthor(rs.getString("author"));
        manga.setTotalChapters(rs.getInt("total_chapters"));
        manga.setCoverUrl(rs.getString("cover_url"));
        manga.setRating(rs.getDouble("rating"));
        manga.setCreatedAt(rs.getTimestamp("created_at"));
        return manga;
    }
}
