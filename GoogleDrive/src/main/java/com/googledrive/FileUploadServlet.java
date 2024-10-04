package com.googledrive;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Base upload directory
    private static final String UPLOAD_DIR = "C:\\uploads"; // Ensure this directory exists

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current user's username from the session
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            // If user is not logged in, redirect to login or show error
            response.getWriter().println("You must be logged in to upload files.");
            return;
        }

        // Use the base upload directory directly
        String userUploadDirPath = UPLOAD_DIR + File.separator + username;

        // Create a user-specific directory if it doesn't exist
        File userUploadDir = new File(userUploadDirPath);
        if (!userUploadDir.exists()) {
            boolean created = userUploadDir.mkdirs(); // Create directories if they don't exist
            if (!created) {
                response.getWriter().println("Failed to create directory for uploads.");
                return;
            }
        }

        // Get the file part from the request
        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);

        // Save the uploaded file in the user-specific directory
        if (fileName != null && !fileName.isEmpty()) {
            String filePath = userUploadDirPath + File.separator + fileName;
            filePart.write(filePath);

            // Store file details in the database
            if (storeFileInDatabase(fileName, username, filePath)) {
            	response.sendRedirect("index.jsp");
            } else {
                response.getWriter().println("File uploaded, but failed to save details to the database.");
            }
        } else {
            response.getWriter().println("File upload failed.");
        }
    }

    // Helper method to extract the file name from the Part object
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return null;
    }

    // Helper method to store file details in the database
    private boolean storeFileInDatabase(String fileName, String owner, String filePath) {
        String query = "INSERT INTO uploaded_files (name, uploaded_on, owner, location) VALUES (?, NOW(), ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fileName);
            preparedStatement.setString(2, owner);
            preparedStatement.setString(3, filePath);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
