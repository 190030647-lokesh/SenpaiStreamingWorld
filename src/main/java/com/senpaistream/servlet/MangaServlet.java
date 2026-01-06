package com.senpaistream.servlet;

import com.senpaistream.dao.MangaDAO;
import com.senpaistream.model.Manga;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/manga")
public class MangaServlet extends HttpServlet {
    private MangaDAO mangaDAO;
    
    @Override
    public void init() {
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
        
        String action = request.getParameter("action");
        String genre = request.getParameter("genre");
        String search = request.getParameter("search");
        
        List<Manga> mangaList;
        
        if (search != null && !search.trim().isEmpty()) {
            mangaList = mangaDAO.searchManga(search);
        } else if (genre != null && !genre.trim().isEmpty()) {
            mangaList = mangaDAO.getMangaByGenre(genre);
        } else {
            mangaList = mangaDAO.getAllManga();
        }
        
        request.setAttribute("mangaList", mangaList);
        response.sendRedirect("manga.html");
    }
}
