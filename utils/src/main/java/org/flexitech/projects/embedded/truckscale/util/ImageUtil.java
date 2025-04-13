package org.flexitech.projects.embedded.truckscale.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageUtil {

    @Value("${image.dir}")
    private String imagePath;

    public String writeImage(MultipartFile file, String subFolder) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : "";
        String newFilename = UUID.randomUUID() + extension;

        Path targetDir = Paths.get(imagePath, subFolder);
        Files.createDirectories(targetDir);

        Path targetPath = targetDir.resolve(newFilename);
        Files.write(targetPath, file.getBytes());

        return newFilename;
    }

    public boolean deleteImage(String storedPath) {
        try {
            Path filePath = Paths.get(imagePath, storedPath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public String getImageUrl(String storedPath) {
        return Paths.get(imagePath, storedPath).toString();
    }
}