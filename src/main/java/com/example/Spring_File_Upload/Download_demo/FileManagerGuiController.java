package com.example.Spring_File_Upload.Download_demo;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class FileManagerGuiController {

    // GET endpoint for serving the file upload page
    @GetMapping("/uploader")
    public String uploader(){
        return "uploader";  // Returns the "uploader" view (HTML page) to the client
    }

    // GET endpoint for listing all files in the storage directory
    @GetMapping("/list_files")
    public String listFiles(Model model) throws IOException {
        // Using DirectoryStream to list files in the specified directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(new File(FileStorageService.STORAGE_DIRECTORY).toPath())) {
            // Adding the list of filenames to the model attribute
            model.addAttribute("files",
                    StreamSupport.stream(stream.spliterator(), false)  // Convert the DirectoryStream to a Stream
                            .map(Path::getFileName)  // Extract file names from paths
                            .map(Path::toString)  // Convert Path objects to Strings
                            .collect(Collectors.toList()));  // Collect the file names into a List
        }
        // Return the "list_files" view (HTML page) to the client, passing the file list
        return "list_files";
    }
}
