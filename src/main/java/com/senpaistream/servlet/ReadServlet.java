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

@WebServlet("/read")
public class ReadServlet extends HttpServlet {
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
            response.sendRedirect("login.jsp");
            return;
        }
        
        String mangaIdStr = request.getParameter("id");
        String chapterStr = request.getParameter("chapter");
        
        if (mangaIdStr == null) {
            response.sendRedirect("manga");
            return;
        }
        
        try {
            int mangaId = Integer.parseInt(mangaIdStr);
            int chapter = (chapterStr != null) ? Integer.parseInt(chapterStr) : 1;
            
            Manga manga = mangaDAO.getMangaById(mangaId);
            
            if (manga == null) {
                response.sendRedirect("manga");
                return;
            }
            
            request.setAttribute("manga", manga);
            request.setAttribute("currentChapter", chapter);
            request.getRequestDispatcher("read.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("manga");
        }
    }
}
