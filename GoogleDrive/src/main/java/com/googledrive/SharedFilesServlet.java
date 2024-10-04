package com.googledrive;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SharedFilesServlet")
public class SharedFilesServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username"); // Assuming you store the username in the session

        FileDAO fileDAO = new FileDAO();
        List<File> sharedFiles = fileDAO.getSharedFiles(username);

        request.setAttribute("sharedFiles", sharedFiles);
        request.getRequestDispatcher("sharedFiles.jsp").forward(request, response);
    }
}
