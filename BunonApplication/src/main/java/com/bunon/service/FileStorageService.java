package com.bunon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/** Saves uploaded pictures into the "uploads" folder and gives back the new file name. */
@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${bunon.upload-dir}") String dir) throws IOException {
        this.uploadDir = Paths.get(dir).toAbsolutePath();
        Files.createDirectories(uploadDir);
    }

    public String save(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
        if (!ext.matches("\\.(jpg|jpeg|png|gif)")) {
            throw new IOException("Please upload a JPG, PNG or GIF picture.");
        }
        String newName = UUID.randomUUID() + ext;        // random name so files never clash
        Files.copy(file.getInputStream(), uploadDir.resolve(newName));
        return newName;
    }

    public Path getUploadDir() { return uploadDir; }
}
