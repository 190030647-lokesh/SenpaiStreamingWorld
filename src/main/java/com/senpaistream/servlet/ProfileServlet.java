package com.senpaistream.servlet;

import com.senpaistream.dao.AnimeDAO;
import com.senpaistream.dao.MangaDAO;
import com.senpaistream.model.Anime;
import com.senpaistream.model.Manga;
import com.senpaistream.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private AnimeDAO animeDAO;
    private MangaDAO mangaDAO;
    
    @Override
    public void init() {
        animeDAO = new AnimeDAO();
        mangaDAO = new MangaDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();
        
        String section = request.getParameter("section");
        
        if ("continue-watching".equals(section)) {
            List<Anime> continueWatching = animeDAO.getContinueWatching(userId);
            request.setAttribute("continueWatching", continueWatching);
            response.sendRedirect("profile.html?section=continue-watching");
            
        } else if ("completed-anime".equals(section)) {
            List<Anime> completedAnime = animeDAO.getCompletedAnime(userId);
            request.setAttribute("completedAnime", completedAnime);
            response.sendRedirect("profile.html?section=completed-anime");
            
        } else if ("continue-reading".equals(section)) {
            List<Manga> continueReading = mangaDAO.getContinueReading(userId);
            request.setAttribute("continueReading", continueReading);
            response.sendRedirect("profile.html?section=continue-reading");
            
        } else if ("completed-manga".equals(section)) {
            List<Manga> completedManga = mangaDAO.getCompletedManga(userId);
            request.setAttribute("completedManga", completedManga);
            response.sendRedirect("profile.html?section=completed-manga");
            
        } else {
            // Default profile page
            response.sendRedirect("profile.html");
        }
    }
}
