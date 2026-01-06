package com.senpaistream.servlet.api;

import com.google.gson.Gson;
import com.senpaistream.dao.AnimeDAO;
import com.senpaistream.model.Anime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/anime/*")
public class AnimeApiServlet extends HttpServlet {
    private AnimeDAO animeDAO;
    private Gson gson = new Gson();
    
    @Override
    public void init() {
        animeDAO = new AnimeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            // Get single anime by ID
            try {
                int animeId = Integer.parseInt(pathInfo.substring(1));
                Anime anime = animeDAO.getAnimeById(animeId);
                
                if (anime != null) {
                    out.print(gson.toJson(anime));
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Anime not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid anime ID\"}");
            }
        } else {
            // Get all anime or filter by genre
            String genre = request.getParameter("genre");
            String search = request.getParameter("search");
            
            if (search != null && !search.trim().isEmpty()) {
                out.print(gson.toJson(animeDAO.searchAnime(search)));
            } else if (genre != null && !genre.trim().isEmpty()) {
                out.print(gson.toJson(animeDAO.getAnimeByGenre(genre)));
            } else {
                out.print(gson.toJson(animeDAO.getAllAnime()));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        
        out.flush();
    }
}
