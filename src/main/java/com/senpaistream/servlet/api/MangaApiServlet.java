package com.senpaistream.servlet.api;

import com.google.gson.Gson;
import com.senpaistream.dao.MangaDAO;
import com.senpaistream.model.Manga;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/manga/*")
public class MangaApiServlet extends HttpServlet {
    private MangaDAO mangaDAO;
    private Gson gson = new Gson();
    
    @Override
    public void init() {
        mangaDAO = new MangaDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            // Get single manga by ID
            try {
                int mangaId = Integer.parseInt(pathInfo.substring(1));
                Manga manga = mangaDAO.getMangaById(mangaId);
                
                if (manga != null) {
                    out.print(gson.toJson(manga));
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Manga not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid manga ID\"}");
            }
        } else {
            // Get all manga or filter by genre
            String genre = request.getParameter("genre");
            String search = request.getParameter("search");
            
            if (search != null && !search.trim().isEmpty()) {
                out.print(gson.toJson(mangaDAO.searchManga(search)));
            } else if (genre != null && !genre.trim().isEmpty()) {
                out.print(gson.toJson(mangaDAO.getMangaByGenre(genre)));
            } else {
                out.print(gson.toJson(mangaDAO.getAllManga()));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        
        out.flush();
    }
}
