package com.example.Spring_File_Upload.Download_demo;

// Marks this class as a service component in Spring — it's a business logic layer class
import org.springframework.stereotype.Service;

// Represents an uploaded file in a multipart/form-data request (commonly used in file uploads)
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Java NIO package to handle file operations like creating, copying, deleting etc.
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service // Indicates that this class is a Spring service bean (used for business logic)
public class FileStorageService {

    // The path where all uploaded files will be stored (you can change it as needed)
    public static final String STORAGE_DIRECTORY = "C:\\Storage";

    // This method handles saving the uploaded file to the storage directory
    public void saveFile(MultipartFile fileToSave) throws IOException {
        if (fileToSave == null) {
            throw new RuntimeException("fileToSave is null"); // Defensive programming check
        }

        // Create a File object representing where the uploaded file should be saved
        // File.separator ensures OS-independent folder structure (on Windows it's "\", on Linux it's "/")
        var targetFile = new File(STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());

        // Security check: Ensures the target file path is inside the intended directory
        // Prevents directory traversal attacks like filename = "../../someSensitiveFile"
        if (!Objects.equals(targetFile.getParent(), STORAGE_DIRECTORY)) {
            throw new SecurityException("Unsupported filename!");
        }

        // Copies file content from the uploaded file (input stream) to the target location
        // REPLACE_EXISTING means it will overwrite any existing file with the same name
        Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    // This method is used to fetch a file for downloading
    public File getDownload(String fileName) throws IOException {
        if (fileName == null) {
            throw new RuntimeException("fileToDownload is null"); // Check for null
        }

        // Create a File object for the requested filename from the storage directory
        var fileToDownload = new File(STORAGE_DIRECTORY + File.separator + fileName);

        // Another security check: ensures file is not outside of the intended directory
        if (!Objects.equals(fileToDownload.getParent(), STORAGE_DIRECTORY)) {
            throw new SecurityException("Unsupported filename!");
        }

        // Check if the file actually exists
        if (!fileToDownload.exists()) {
            throw new FileNotFoundException("No file named: " + fileName);
        }

        // Return the file to the caller (usually for downloading)
        return fileToDownload;
    }
}
