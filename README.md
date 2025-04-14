Spring File Upload Demo
This project demonstrates a simple file upload and download system using Spring Boot. It includes functionalities for uploading files, listing stored files, and downloading them either through a regular or faster approach. The application also uses MultipartFile for handling file uploads, and provides a simple front-end interface for interacting with these operations.

Features
File Upload: Upload files via a POST request.

File List: View all files stored in the system.

File Download: Download files using GET requests, with an option for faster download.

Error Handling: Logs and handles errors for both uploading and downloading files.

Technologies Used
-Spring Boot
-Spring MVC
-Java NIO (for file handling)
-Thymeleaf/JSP (for views)

File Upload Page
Navigate to /uploader to access the file upload page.

List Files Page
Navigate to /list_files to view the list of uploaded files.

Code Explanation
MultipartFile:
This is used to handle file uploads in Spring. The file data is passed in the HTTP request as part of a multi-part form.

FileStorageService:
It's responsible for saving and retrieving files from storage (like a local directory or cloud storage).

ResponseEntity:
This represents the entire HTTP response, including status code, headers, and body. It allows you to customize the response in your endpoints, for example, to specify content type and file download headers.

FileSystemResource vs. InputStreamResource:
FileSystemResource is more efficient for serving files directly from the file system since it represents a file on the system.
InputStreamResource is useful when you need to return an InputStream to stream the file content from the server.

Files.probeContentType:
This method is used to detect the MIME type of the file based on its contents. It is used here to set the correct Content-Type header in the response.

Model:
The Model is a container for adding attributes (data) that will be rendered on the view (HTML page). In your code, it is used to add the list of filenames to the model, which is then accessible in the view (like Thymeleaf, JSP, etc.).

DirectoryStream:
This class is used to iterate over the contents of a directory. In your code, it is used to read the files in the directory where files are stored.

StreamSupport.stream():
This is used to convert a DirectoryStream (which is not a Stream) into a Stream so that you can apply Java Stream API operations (like map and collect) on it.

Path::getFileName and Path::toString:
getFileName extracts just the file name (without the full path), and toString converts the Path object into a String. These operations are part of the Path class in the java.nio.file package.

Returning the View Name:
The controller methods return strings such as "uploader" or "list_files", which correspond to view names (likely referring to .html or .jsp templates).

Endpoints
/uploader
When the user visits this URL, the method uploader() is triggered, which returns the view "uploader". This is the page where the user can upload a file.

/list_files
This method reads all the files in the directory specified by FileStorageService.STORAGE_DIRECTORY and adds the filenames to the model object. Then it returns the "list_files" view, which will display the list of files.

/download
Use this endpoint to download a file. The file will be returned with the appropriate Content-Type header and as an attachment.

/download-faster
This endpoint is similar to /download but uses FileSystemResource for faster file serving from the file system.
