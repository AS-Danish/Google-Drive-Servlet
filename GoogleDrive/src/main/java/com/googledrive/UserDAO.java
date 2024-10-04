package com.googledrive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to check if a user exists by their username
    public boolean doesUserExist(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Get the count of matching users
                return count > 0; // If count > 0, the user exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
        return false; // Return false if user does not exist
    }
}
