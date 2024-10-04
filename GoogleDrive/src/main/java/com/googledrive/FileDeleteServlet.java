package com.googledrive;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FileDeleteServlet")
public class FileDeleteServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int fileId = Integer.parseInt(request.getParameter("fileId"));
	    FileDAO fileDAO = new FileDAO();
	    com.googledrive.File fileToDelete = fileDAO.getFileById(fileId);

	    if (fileToDelete != null) {
	        // Delete shared file records before deleting the file
	        fileDAO.deleteSharedFilesByFileId(fileId);
	        
	        // Delete file from filesystem
	        java.io.File file = new java.io.File(fileToDelete.getLocation());
	        if (file.delete()) {
	            System.out.println("File deleted successfully from the filesystem.");
	        } else {
	            System.out.println("Failed to delete the file from the filesystem.");
	        }

	        // Delete file record from the database
	        fileDAO.deleteFile(fileId);
	        response.sendRedirect("index.jsp");
	    } else {
	        response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
	    }
	}

}
