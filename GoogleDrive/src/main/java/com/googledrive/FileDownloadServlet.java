package com.googledrive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FileDownloadServlet")
public class FileDownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileIdStr = request.getParameter("fileId");

        if (fileIdStr == null || fileIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File ID is missing.");
            return;
        }

        int fileId = Integer.parseInt(fileIdStr);
        String filePath = getFilePath(fileId);

        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                response.setContentLength((int) file.length());

                try (FileInputStream fileInputStream = new FileInputStream(file);
                     OutputStream out = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found in database.");
        }
    }

    private String getFilePath(int fileId) {
        String query = "SELECT location FROM uploaded_files WHERE id = ?";
        String filePath = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, fileId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                filePath = resultSet.getString("location");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }

        return filePath;
    }
}
