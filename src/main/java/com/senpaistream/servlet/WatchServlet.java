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

@WebServlet("/watch")
public class WatchServlet extends HttpServlet {
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
            response.sendRedirect("login.jsp");
            return;
        }
        
        String animeIdStr = request.getParameter("id");
        String episodeStr = request.getParameter("episode");
        
        if (animeIdStr == null) {
            response.sendRedirect("anime");
            return;
        }
        
        try {
            int animeId = Integer.parseInt(animeIdStr);
            int episode = (episodeStr != null) ? Integer.parseInt(episodeStr) : 1;
            
            Anime anime = animeDAO.getAnimeById(animeId);
            
            if (anime == null) {
                response.sendRedirect("anime");
                return;
            }
            
            request.setAttribute("anime", anime);
            request.setAttribute("currentEpisode", episode);
            request.getRequestDispatcher("watch.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("anime");
        }
    }
}
