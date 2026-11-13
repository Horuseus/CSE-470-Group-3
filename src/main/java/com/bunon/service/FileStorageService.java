package com.bunon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

/** Saves uploads (product images, designs, NID documents) in the upload folder. */
@Service
public class FileStorageService {

    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "pdf");

    private final Path root;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /** @return relative path like "nid/uuid.jpg", or null if no file was uploaded */
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (ext == null || !ALLOWED.contains(ext.toLowerCase())) {
            throw new IllegalArgumentException("Only JPG, PNG or PDF files are allowed");
        }
        try {
            Path dir = root.resolve(subDir);
            Files.createDirectories(dir);
            String name = UUID.randomUUID() + "." + ext.toLowerCase();
            Files.copy(file.getInputStream(), dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
            return subDir + "/" + name;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not store file", e);
        }
    }

    public Resource load(String relativePath) {
        try {
            Path file = root.resolve(relativePath).normalize();
            if (!file.startsWith(root)) {
                throw new IllegalArgumentException("Invalid path");
            }
            return new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid path", e);
        }
    }
}
