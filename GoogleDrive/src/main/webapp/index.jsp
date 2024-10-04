<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="com.googledrive.File"%>
<%@ page import="com.googledrive.FileDAO"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Google Drive</title>
<script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
	integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body>

	<%
	// Check if the user is logged in
	String username = (String) session.getAttribute("username");
	if (username == null) {
		response.sendRedirect("sign-in.html"); // Redirect to login page if not logged in
		return; // Stop further processing
	}
	%>

	<button data-drawer-target="logo-sidebar"
		data-drawer-toggle="logo-sidebar" aria-controls="logo-sidebar"
		type="button"
		class="inline-flex items-center p-2 mt-2 ms-3 text-sm text-gray-500 rounded-lg sm:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600">
		<span class="sr-only">Open sidebar</span>
		<svg class="w-6 h-6" aria-hidden="true" fill="currentColor"
			viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
            <path clip-rule="evenodd" fill-rule="evenodd"
				d="M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z">
            </path>
        </svg>
	</button>

	<aside id="logo-sidebar"
		class="fixed top-0 left-0 z-40 w-64 h-screen transition-transform -translate-x-full sm:translate-x-0"
		aria-label="Sidebar">
		<div
			class="h-full px-3 py-4 overflow-y-auto bg-gray-50 dark:bg-gray-800">
			<a href="index.jsp" class="flex items-center ps-2.5 mb-5"> <img
				src="GoogleDrive.png" class="h-6 me-3 sm:h-7" alt="Logo" /> <span
				class="self-center text-xl font-semibold whitespace-nowrap dark:text-white">Google
					Drive</span>
			</a>

			<!-- Upload Form -->
			<form action="FileUploadServlet" method="post"
				enctype="multipart/form-data">
				<div class="inline-block my-5">
					<label for="file-upload"
						class="text-gray-900 bg-white border border-gray-300 focus:outline-none hover:bg-gray-100 focus:ring-4 focus:ring-gray-100 font-medium rounded-xl text-sm px-10 py-5 me-2 dark:bg-gray-800 dark:text-white dark:border-gray-600 dark:hover:bg-gray-700 dark:hover:border-gray-600 dark:focus:ring-gray-700 cursor-pointer">
						<span><i class="fa-solid fa-plus"></i></span> New
					</label> <input id="file-upload" type="file" name="file" class="hidden"
						onchange="this.form.submit();" />
				</div>
			</form>

			<ul class="space-y-2 font-medium">
				<li><a href="#"
					class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
						<i class="fa-solid fa-house"></i> <span class="ms-3">Home</span>
				</a></li>
				<li><a href="#"
					class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
						<i class="fa-solid fa-file-shield"></i> <span
						class="flex-1 ms-3 whitespace-nowrap">My Drive</span>
				</a></li>
				<li><a href="#"
					class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
						<i class="fa-regular fa-star"></i> <span
						class="flex-1 ms-3 whitespace-nowrap">Starred</span>
				</a></li>
				<li><a href="SharedFilesServlet"
					class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
						<i class="fa-solid fa-user-group"></i> <span
						class="flex-1 ms-3 whitespace-nowrap">Shared with Me</span>
				</a></li>
				<li><a href="invokeFiles.jsp"
					class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
						<i class="fa-solid fa-user-group"></i> <span
						class="flex-1 ms-3 whitespace-nowrap">My Shared Files</span>
				</a></li>
				<li><a href="logout"
					class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
						<i class="fa-solid fa-right-from-bracket"></i> <span
						class="flex-1 ms-3 whitespace-nowrap">Logout</span>
				</a></li>
			</ul>
		</div>
	</aside>

	<div class="p-4 sm:ml-64">
		<div class="flex items-center justify-between mb-4">
			<h1 class="text-xl my-2">
				Welcome,
				<%=username%></h1>
			<div class="w-10 h-10 rounded-full overflow-hidden">
				<img src="https://via.placeholder.com/40" alt="User Profile"
					class="w-full h-full object-cover" />
			</div>
		</div>

		<div
			class="p-4 border-2 border-gray-200 border-dashed rounded-lg dark:border-gray-700">
			<div class="relative overflow-x-auto shadow-md sm:rounded-lg">
				<table
					class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
					<thead
						class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
						<tr>
							<th scope="col" class="px-6 py-3">Name</th>
							<th scope="col" class="px-6 py-3">Uploaded On</th>
							<th scope="col" class="px-6 py-3">Owner</th>
							<th scope="col" class="px-6 py-3">Location</th>
							<th scope="col" class="px-6 py-3">Action</th>
						</tr>
					</thead>
					<tbody>
						<%
						FileDAO fileDAO = new FileDAO();
						List<File> uploadedFiles = fileDAO.getFilesByOwner(username); // Get user's uploaded files
						List<File> sharedFiles = fileDAO.getSharedFiles(username); // Get user's shared files

						HashSet<Integer> sharedFileIds = new HashSet<>();

						// Display uploaded files
						for (File file : uploadedFiles) {
						%>
						<tr
							class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
							<th scope="row"
								class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
								<%=file.getName()%>
							</th>
							<td class="px-6 py-4"><%=file.getUploadedOn()%></td>
							<td class="px-6 py-4"><%=file.getOwner()%></td>
							<td class="px-6 py-4">My Drive</td>
							<td class="px-6 py-4">
								<button
									onclick="window.location.href='FileDownloadServlet?fileId=<%=file.getId()%>'"
									class="bg-blue-600 text-white font-medium rounded-md px-4 py-2 hover:bg-blue-700 transition duration-200 ease-in-out">
									<i class="fa-solid fa-download"></i> Download
								</button>

								<form action="FileDeleteServlet" method="post"
									class="inline-block">
									<input type="hidden" name="fileId" value="<%=file.getId()%>" />
									<button type="submit"
										class="bg-red-600 text-white font-medium rounded-md px-4 py-2 hover:bg-red-700 transition duration-200 ease-in-out">
										<i class="fa-solid fa-trash"></i> Delete
									</button>
								</form>

								<button onclick="shareFile(<%=file.getId()%>)"
									class="bg-green-600 text-white font-medium rounded-md px-4 py-2 hover:bg-green-700 transition duration-200 ease-in-out">
									<i class="fa-solid fa-share"></i> Share
								</button>
							</td>
						</tr>
						<%
						}

						// Add the shared file IDs to the Set
						for (File file : sharedFiles) {
						sharedFileIds.add(file.getId());
						}

						// Display shared files without duplicates
						for (File file : sharedFiles) {
						if (!sharedFileIds.contains(file.getId()))
							continue; // Skip already displayed shared files
						%>
						<tr
							class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
							<th scope="row"
								class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
								<%=file.getName()%>
							</th>
							<td class="px-6 py-4"><%=file.getUploadedOn()%></td>
							<td class="px-6 py-4"><%=file.getOwner()%></td>
							<td class="px-6 py-4"><%=file.getLocation()%></td>
							<td class="px-6 py-4">
								<button
									onclick="window.location.href='FileDownloadServlet?fileId=<%=file.getId()%>'"
									class="bg-blue-600 text-white font-medium rounded-md px-4 py-2 hover:bg-blue-700 transition duration-200 ease-in-out">
									<i class="fa-solid fa-download"></i> Download
								</button>
							</td>
						</tr>
						<%
						}
						%>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script>
	function shareFile(fileId) {
	    const username = prompt("Enter the username to share the file with:");
	    if (username) {
	        fetch('ShareFileServlet?fileId=' + fileId + '&username=' + username)
	            .then(response => response.json())
	            .then(data => {
	                if (data.success) {
	                    alert('File shared successfully!');
	                } else {
	                    alert('Failed to share the file.');
	                }
	            })
	            .catch(error => alert('Error: ' + error));
	    }
	}

	</script>

</body>

</html>