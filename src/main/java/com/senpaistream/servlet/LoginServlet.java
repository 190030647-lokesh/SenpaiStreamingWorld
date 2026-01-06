package com.senpaistream.servlet;

import com.senpaistream.dao.UserDAO;
import com.senpaistream.model.User;
import com.senpaistream.util.DatabaseConnection;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET requests not supported - redirect to login.html
        response.sendRedirect("login.html");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Email and password are required");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        // Validate email format - @gmail.com required
        if (!email.contains("@gmail.com")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Please enter a valid Gmail address");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        // Check database connection
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                // Demo mode - allow any login
                System.out.println("Demo mode: Login for " + email);
                String username = email.split("@")[0]; // Get username from email
                
                HttpSession session = request.getSession();
                session.setAttribute("userId", 1);
                session.setAttribute("username", username);
                session.setAttribute("email", email);
                session.setAttribute("demoMode", true);
                session.setMaxInactiveInterval(30 * 60);
                
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Login successful! (Demo Mode)");
                jsonResponse.addProperty("username", username);
                jsonResponse.addProperty("email", email);
                jsonResponse.addProperty("redirect", "index.html");
                jsonResponse.addProperty("demoMode", true);
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }
        } catch (Exception dbError) {
            // Database connection failed - use demo mode
            System.err.println("Database connection error, using demo mode: " + dbError.getMessage());
            String username = email.split("@")[0];
            
            HttpSession session = request.getSession();
            session.setAttribute("userId", 1);
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("demoMode", true);
            session.setMaxInactiveInterval(30 * 60);
            
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Login successful! (Demo Mode)");
            jsonResponse.addProperty("username", username);
            jsonResponse.addProperty("email", email);
            jsonResponse.addProperty("redirect", "index.html");
            jsonResponse.addProperty("demoMode", true);
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        try {
            User user = userDAO.loginUser(email, password);
            
            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("email", user.getEmail());
                
                // Set session timeout (30 minutes)
                session.setMaxInactiveInterval(30 * 60);
                
                // If remember me is checked, create cookies
                if ("on".equals(remember)) {
                    Cookie emailCookie = new Cookie("userEmail", email);
                    emailCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    response.addCookie(emailCookie);
                }
                
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Login successful!");
                jsonResponse.addProperty("username", user.getUsername());
                jsonResponse.addProperty("email", user.getEmail());
                jsonResponse.addProperty("redirect", "index.html");
                jsonResponse.addProperty("demoMode", false);
                out.print(jsonResponse.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Invalid email or password");
                out.print(jsonResponse.toString());
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "An error occurred during login");
            out.print(jsonResponse.toString());
            e.printStackTrace();
        }
        out.flush();
    }
}

