package com.googledrive;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RevokeAccessServlet")
public class RevokeAccessServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FileDAO fileDAO; // Assume you have a FileDAO instance for database operations

    @Override
    public void init() throws ServletException {
        super.init();
        fileDAO = new FileDAO(); // Initialize your DAO here
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileIdString = request.getParameter("fileId");

        if (fileIdString == null || fileIdString.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File ID is missing or invalid.");
            return;
        }

        try {
            int fileId = Integer.parseInt(fileIdString);
            String username = request.getParameter("username");
            System.out.println("Received fileId: " + fileId);

            if (username == null || username.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is missing.");
                return;
            }

            // Call the method to unshare the file
            fileDAO.unshareFile(fileId, username);

            // Optionally, send a success response
            response.sendRedirect("invokeFiles.jsp");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid File ID format.");

        } catch (Exception e) {
            // Handle other exceptions
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while revoking access.");
            e.printStackTrace(); // Log the exception for debugging
        }
    }
}
