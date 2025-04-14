package com.example.Spring_File_Upload.Download_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FileManagerController {

    // Auto-wiring the FileStorageService to handle file storage operations
    @Autowired
    private FileStorageService fileStorageService;

    // Logger for logging the activities of the controller
    private static final Logger log = Logger.getLogger(FileManagerController.class.getName());

    // POST endpoint to upload a file
    @PostMapping("/upload-file")
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Calling the saveFile method from fileStorageService to save the file
            fileStorageService.saveFile(file);
            System.out.println("Uploading file: " + file.getOriginalFilename());
        } catch (IOException e) {
            // Logging error in case of an exception during file upload
            log.log(Level.SEVERE, "Exception during upload", e);
        }
    }

    // GET endpoint to download a file
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName){
        try {
            // Getting the file to be downloaded from the storage service
            var fileToDownLoad = fileStorageService.getDownload(fileName);

            // Returning the file as a response entity with necessary headers and content type
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentLength(fileToDownLoad.length())
                    .contentType(MediaType.parseMediaType(Files.probeContentType(fileToDownLoad.toPath())))  // Detecting content type
                    .body(new InputStreamResource(Files.newInputStream(fileToDownLoad.toPath())));  // Wrapping the file input stream in a resource
        } catch (IOException e) {
            // Logging error in case of an exception during file download
            log.log(Level.SEVERE, "Exception during download", e);
            return ResponseEntity.notFound().build();  // Returning 404 if file is not found
        }
    }

    // Alternative GET endpoint for faster file download using FileSystemResource
    @GetMapping("/download-faster")
    public ResponseEntity<Resource> downloadFileFaster(@RequestParam("fileName") String fileName){
        try {
            // Getting the file to be downloaded from the storage service
            var fileToDownLoad = fileStorageService.getDownload(fileName);

            // Returning the file as a response entity with necessary headers and content type
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentLength(fileToDownLoad.length())
                    .contentType(MediaType.parseMediaType(Files.probeContentType(fileToDownLoad.toPath())))  // Detecting content type
                    .body(new FileSystemResource(fileToDownLoad));  // Wrapping the file in a FileSystemResource for faster download
        } catch (IOException e) {
            // Logging error in case of an exception during file download
            log.log(Level.SEVERE, "Exception during download", e);
            return ResponseEntity.notFound().build();  // Returning 404 if file is not found
        }
    }
}
