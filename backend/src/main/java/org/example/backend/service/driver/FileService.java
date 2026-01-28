package org.example.backend.service.driver;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final String UPLOAD_DIR = "files/";
    private final long MAX_FILE_SIZE = 1024 * 1024; // 1 MB in bytes

    public String saveFile(MultipartFile file) throws IOException {
        // Create a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Ensure the directory exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the original file temporarily
        Path tempFilePath = uploadPath.resolve("temp_" + fileName);
        Files.write(tempFilePath, file.getBytes());

        // Compress the file if it's larger than 1MB
        Path filePath = uploadPath.resolve(fileName);
        if (Files.size(tempFilePath) > MAX_FILE_SIZE) {
            // Compress the file to bring it down to kilobytes
            Thumbnails.of(tempFilePath.toFile())
                    .size(1024, 1024)  // Resize the image
                    .outputQuality(0.7)  // Adjust quality (70%)
                    .toFile(filePath.toFile());
        } else {
            // If the file is already small enough, just save it as is
            Files.move(tempFilePath, filePath);
        }

        // Delete the temporary file
        Files.deleteIfExists(tempFilePath);

        return fileName;
    }
}
