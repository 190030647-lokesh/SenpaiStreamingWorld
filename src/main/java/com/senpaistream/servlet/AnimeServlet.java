package com.senpaistream.servlet;

import com.senpaistream.dao.AnimeDAO;
import com.senpaistream.model.Anime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/anime")
public class AnimeServlet extends HttpServlet {
    private AnimeDAO animeDAO;
    
    @Override
    public void init() {
        animeDAO = new AnimeDAO();
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
        
        String action = request.getParameter("action");
        String genre = request.getParameter("genre");
        String search = request.getParameter("search");
        
        List<Anime> animeList;
        
        if (search != null && !search.trim().isEmpty()) {
            animeList = animeDAO.searchAnime(search);
        } else if (genre != null && !genre.trim().isEmpty()) {
            animeList = animeDAO.getAnimeByGenre(genre);
        } else {
            animeList = animeDAO.getAllAnime();
        }
        
        request.setAttribute("animeList", animeList);
        response.sendRedirect("anime.html");
    }
}
