package org.example.backend.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/fileController")
@CrossOrigin
public class FileController {
    @GetMapping("/photo")
    public void getFile(@RequestParam String img, HttpServletResponse response) throws IOException {
        FileInputStream inputStream = new FileInputStream("files/" + img);
        ServletOutputStream outputStream = response.getOutputStream();
        inputStream.transferTo(outputStream);
        inputStream.close();
        outputStream.close();
    }

    @PostMapping("/photo")
    public String saveFile(@RequestParam MultipartFile file) throws IOException {
        // Noyob fayl nomini yaratilmoqda
        String img = UUID.randomUUID() + file.getOriginalFilename();
        File outputFile = new File("files/" + img);

        // Asl fayl o'lchamini olish
        long originalSize = file.getSize();

        // Boshlang'ich sifat va ko'lam
        double scale = 0.5;
        double quality = 0.8;

        // Faylni yaratish va sifatni kamaytirish orqali 29KB ga yetkazish
        do {
            Thumbnails.of(file.getInputStream())
                    .scale(scale)  // Rasm o'lchamini kamaytiradi
                    .outputQuality(quality) // Rasm sifatini sozlaydi
                    .toFile(outputFile);

            // Yangi fayl o'lchamini olish
            long compressedSize = outputFile.length();

            // Agar fayl o'lchami 29KB dan katta bo'lsa, sifatni kamaytirish
            if (compressedSize > 29 * 1024) {
                quality -= 0.05; // Sifatni kamaytirish
                scale -= 0.05;   // O'lchamni ham kamaytirish
            } else {
                break; // Kichikroq o'lchamga yetishildi
            }
        } while (quality > 0 && scale > 0); // Sifat va o'lchamni chegara doirasida ushlab turish

        // Rasmning yakuniy o'lchami
        long compressedSize = outputFile.length();
        System.out.println("Original size: " + originalSize);
        System.out.println("Compressed size: " + compressedSize);

        return img;
    }
}