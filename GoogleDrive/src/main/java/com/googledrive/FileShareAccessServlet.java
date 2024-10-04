package com.googledrive;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FileShareAccessServlet")
public class FileShareAccessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the file ID from the request parameter
        String fileId = request.getParameter("fileId");

        if (fileId != null) {
            // Here, we could have logic to fetch available users or handle file-sharing logic
            request.setAttribute("fileId", fileId);

            // Forward to a JSP page where the user can select whom to share the file with
            request.getRequestDispatcher("shareFile.jsp").forward(request, response);
        } else {
            // Handle case where fileId is missing
            response.sendRedirect("error.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // In the POST method, we handle the actual file sharing logic

        // Retrieve parameters from the form
        String fileId = request.getParameter("fileId");
        String sharedUsername = request.getParameter("sharedUsername");

        if (fileId != null && sharedUsername != null) {
            FileDAO fileDAO = new FileDAO();

            // Share the file with the specified user
            fileDAO.shareFileWithUser(Integer.parseInt(fileId), sharedUsername);

            // Redirect to the shared files page after sharing
            response.sendRedirect("sharedFiles.jsp");
        } else {
            // Handle missing parameters
            response.sendRedirect("error.jsp");
        }
    }
}
