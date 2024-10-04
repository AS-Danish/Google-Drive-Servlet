package com.googledrive;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password); // Use hashing in production
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create a session and store the username
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Redirect to index.html
                response.sendRedirect("index.jsp");
            } else {
                response.getWriter().print("Invalid credentials!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("Login failed!");
        }
    }
}
