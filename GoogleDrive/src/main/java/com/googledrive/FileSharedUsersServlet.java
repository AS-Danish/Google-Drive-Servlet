package com.googledrive;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FileSharedUsersServlet")
public class FileSharedUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the file ID from the request parameter
        String fileId = request.getParameter("fileId");

        if (fileId != null) {
            FileDAO fileDAO = new FileDAO();

            // Get the list of users with whom the file is shared
            List<String> sharedUsers = fileDAO.getSharedUsersByFileId(Integer.parseInt(fileId));

            // Set the shared users list as a request attribute
            request.setAttribute("sharedUsers", sharedUsers);
            request.setAttribute("fileId", fileId);

            // Forward to the JSP page that will display the shared users
            request.getRequestDispatcher("sharedUsers.jsp").forward(request, response);
        } else {
            // Handle case where fileId is missing
            response.sendRedirect("error.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
