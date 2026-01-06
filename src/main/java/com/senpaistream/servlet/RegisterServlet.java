package com.senpaistream.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.senpaistream.dao.UserDAO;
import com.senpaistream.model.User;
import com.senpaistream.util.DatabaseConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;
    private Gson gson = new Gson();
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("register.html");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (username == null || username.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Username is required");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Email is required");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        if (!email.contains("@gmail.com")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Please enter a correct Gmail address");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        if (password == null || password.length() < 6) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Password must be at least 6 characters");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Passwords do not match");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        // Check database connection first
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            // Demo mode - allow registration without database
            System.out.println("Demo mode: Registration for " + username);
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("demoMode", true);
            
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Registration successful! (Demo Mode) Please login.");
            jsonResponse.addProperty("demoMode", true);
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        // Database is available - use normal registration
        try {
            // Check if username exists
            if (userDAO.usernameExists(username)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Username already exists");
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }
            
            // Check if email exists
            if (userDAO.emailExists(email)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Email already registered");
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }
        
            User newUser = new User(username, email, password);
            boolean registered = userDAO.registerUser(newUser);
            
            if (registered) {
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Registration successful! Please login.");
                out.print(jsonResponse.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Registration failed. Please try again.");
                out.print(jsonResponse.toString());
            }
        } catch (Exception e) {
            // Fallback to demo mode on any error
            System.err.println("Registration error, using demo mode: " + e.getMessage());
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("demoMode", true);
            
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Registration successful! (Demo Mode)");
            jsonResponse.addProperty("demoMode", true);
            out.print(jsonResponse.toString());
        }
        
        out.flush();
    }
}
