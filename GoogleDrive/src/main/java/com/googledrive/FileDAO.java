package com.googledrive;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

    // Method to get all uploaded files
    public List<File> getAllFiles() {
        List<File> files = new ArrayList<>();
        String query = "SELECT * FROM uploaded_files"; // Your SQL query to get files

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String uploadedOn = resultSet.getString("uploaded_on"); // Adjust column names as per your DB
                String owner = resultSet.getString("owner");
                String location = resultSet.getString("location");

                File file = new File(id, name, uploadedOn, owner, location);
                files.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
        return files;
    }

    // Method to delete a file
    public void deleteFile(int fileId) {
        String query = "DELETE FROM uploaded_files WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, fileId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
    }

    // Method to get a file by its ID
    public File getFileById(int fileId) {
        File file = null;
        String query = "SELECT * FROM uploaded_files WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, fileId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String uploadedOn = resultSet.getString("uploaded_on");
                String owner = resultSet.getString("owner");
                String location = resultSet.getString("location");

                file = new File(id, name, uploadedOn, owner, location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return file;
    }

    // Method to get all uploaded files by a specific owner
    public List<File> getFilesByOwner(String owner) {
        List<File> files = new ArrayList<>();
        String query = "SELECT * FROM uploaded_files WHERE owner = ?"; // Updated SQL query

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, owner); // Set the owner in the prepared statement
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String uploadedOn = resultSet.getString("uploaded_on"); // Adjust column names as per your DB
                String location = resultSet.getString("location");

                File file = new File(id, name, uploadedOn, owner, location);
                files.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
        return files;
    }

    // Method to get files shared with a user
    public List<File> getSharedFiles(String username) {
        List<File> sharedFiles = new ArrayList<>();
        String sql = "SELECT f.id, f.name, f.uploaded_on, f.owner FROM uploaded_files f " +
                     "JOIN shared_files sf ON f.id = sf.file_id " +
                     "WHERE sf.shared_with = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Use the constructor to create the file object
                File file = new File(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("uploaded_on"), // Assuming uploadedOn is still a String
                    resultSet.getString("owner"),
                    "Shared With Me" // Static value for location
                );
                sharedFiles.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sharedFiles;
    }

    // Method to get files shared by the current user
    public List<File> getFilesSharedByUser(String owner) {
        List<File> filesSharedByMe = new ArrayList<>();
        String sql = "SELECT f.id, f.name, f.uploaded_on FROM uploaded_files f " +
                     "JOIN shared_files sf ON f.id = sf.file_id " +
                     "WHERE f.owner = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                File file = new File(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("uploaded_on"),
                    owner,
                    "Shared By Me"
                );
                filesSharedByMe.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesSharedByMe;
    }

    // Method to get the list of users a file has been shared with
    public List<String> getSharedUsersByFileId(int fileId) {
        List<String> sharedUsers = new ArrayList<>();
        String query = "SELECT shared_with FROM shared_files WHERE file_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, fileId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                sharedUsers.add(resultSet.getString("shared_with"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
        return sharedUsers; // Return the list of shared users
    }

    // Method to delete shared files by file ID
    public void deleteSharedFilesByFileId(int fileId) {
        String sql = "DELETE FROM shared_files WHERE file_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fileId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to unshare a file with a specific user
    public void unshareFile(int fileId, String sharedWith) {
        String sql = "DELETE FROM shared_files WHERE file_id = ? AND shared_with = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fileId);
            stmt.setString(2, sharedWith);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to share a file with a specific user
    public void shareFile(int fileId, String sharedWith) {
        String sql = "INSERT INTO shared_files (file_id, shared_with) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fileId);
            stmt.setString(2, sharedWith);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void shareFileWithUser(int fileId, String sharedUsername) {
        String query = "INSERT INTO shared_files (file_id, username) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, fileId);
            ps.setString(2, sharedUsername);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean removeShareAccess(String fileId, String username) {
        boolean isRemoved = false;
        String query = "DELETE FROM shared_files WHERE file_id = ? AND shared_with = ?"; // Adjust table and column names as necessary

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fileId);
            stmt.setString(2, username);
            isRemoved = stmt.executeUpdate() > 0; // Execute the query and check if rows were affected
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }

        return isRemoved;
    }
    
    
    public void removeFileFromUserDirectory(int fileId, String username) throws SQLException {
        String filePath = getFilePathForUser(fileId, username);
        java.io.File file = new java.io.File(filePath); // Use java.io.File for file operations

        if (file.exists()) {
            file.delete(); // Delete the file from the user's directory
        }
    }


    // Unshare the file with the specific user
    public void unshareFileWithUser(int fileId, String username) throws SQLException {
        String query = "DELETE FROM shared_files WHERE file_id = ? AND username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileId);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    // Helper method to get the file path for a specific user
    private String getFilePathForUser(int fileId, String username) throws SQLException {
        String filePath = null;
        String query = "SELECT file_location FROM files WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String originalPath = rs.getString("file_location");
                // Assuming file is stored in C:/uploads/{username}/ directory
                filePath = originalPath.replace("C:/uploads/original_user/", "C:/uploads/" + username + "/");
            }
        }
        return filePath;
    }


}
