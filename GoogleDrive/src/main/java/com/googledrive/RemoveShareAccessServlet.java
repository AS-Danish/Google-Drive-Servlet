package com.googledrive;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RemoveShareAccessServlet")
public class RemoveShareAccessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileId = request.getParameter("fileId");
        String username = request.getParameter("username");

        FileDAO fileDAO = new FileDAO();
        
        // Remove access from the database
        boolean success = fileDAO.removeShareAccess(fileId, username);

        if (success) {
            request.setAttribute("message", "Access removed successfully.");
        } else {
            request.setAttribute("message", "Failed to remove access.");
        }
        
        // Redirect back to the shared users page
        request.getRequestDispatcher("FileSharedUsersServlet?fileId=" + fileId).forward(request, response);
    }
}
