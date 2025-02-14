package com.potential.api.common.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageStorageComponent {
    @Value("${file.upload.directory}")
    private String directory;

    public String saveImage(MultipartFile image, String path) throws IOException {
        String fileName;

        if (image == null || image.isEmpty()) {
            fileName = "defaultUserProfile.png";
        } else {
            fileName = UUID.randomUUID() + ".jpg";
        }

        String imagePath = directory + path + fileName;

        if (image != null && !image.isEmpty()) {
            Files.write(Paths.get(imagePath), image.getBytes());
        }

        return imagePath;
    }
}