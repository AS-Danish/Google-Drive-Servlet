package com.googledrive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ShareFileServlet")
public class ShareFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileId = Integer.parseInt(request.getParameter("fileId"));
        String sharedWithUsername = request.getParameter("username"); // The username of the person you're sharing with

        // Step 1: Check if the user exists
        UserDAO userDAO = new UserDAO();
        boolean userExists = userDAO.doesUserExist(sharedWithUsername);

        if (!userExists) {
            // If the user doesn't exist, return an error response
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"User does not exist.\"}");
            return;
        }

        // Step 2: Get the file information (from FileDAO)
        FileDAO fileDAO = new FileDAO();
        com.googledrive.File fileToShare = fileDAO.getFileById(fileId);

        if (fileToShare == null) {
            // If the file doesn't exist, return an error response
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"File not found.\"}");
            return;
        }

     // Step 3: Set the shared user directory
        String originalFilePath = fileToShare.getLocation(); // Original file path
        String sharedUserDirectory = "C:\\uploads\\" + sharedWithUsername; // Directory for the shared user

        // Create the directory if it doesn't exist
        File userDir = new File(sharedUserDirectory);
        if (!userDir.exists()) {
            userDir.mkdirs(); // Create the user's directory
        }

        // Copy the file to the shared user's folder
        File sourceFile = new File(originalFilePath);
        File destinationFile = new File(userDir, sourceFile.getName()); // Same file name in the new user's directory

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to copy file.\"}");
            return;
        }

        // Step 4: Share the file by inserting a record in the shared_files table
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO shared_files (file_id, shared_with) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, fileId);
            statement.setString(2, sharedWithUsername);
            statement.executeUpdate();

            // Return success response
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"File shared successfully and uploaded to user's directory!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // Return failure response
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to share file.\"}");
        }
    }
}
