
package com.esg.esgbackend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceController {

    private final Path uploadDir =
            Paths.get("uploads", "evidence").toAbsolutePath().normalize();

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadEvidence(
            @RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Please select a file"));
        }

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(
                    originalName.lastIndexOf(".")).toLowerCase();
        }

        if (!java.util.Set.of(".pdf", ".png", ".jpg", ".jpeg", ".csv")
                .contains(extension)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "File type not allowed"));
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Maximum file size is 5 MB"));
        }

        try {
            Files.createDirectories(uploadDir);

            String storedName = UUID.randomUUID() + extension;
            Path destination = uploadDir.resolve(storedName).normalize();

            if (!destination.startsWith(uploadDir)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid filename"));
            }

            Files.copy(file.getInputStream(), destination);
            
            Properties metadata = new Properties();
            metadata.setProperty(
                "originalFilename",
                originalName == null ? "unnamed" : Paths.get(originalName).getFileName().toString()
            );

            try (OutputStream out = Files.newOutputStream(
                    uploadDir.resolve(storedName + ".meta"))) {
                metadata.store(out, "Evidence file metadata");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Evidence uploaded successfully",
                    "originalFilename", originalName == null
                            ? "unnamed" : Paths.get(originalName).getFileName().toString(),
                    "storedFilename", storedName,
                    "sizeBytes", file.getSize(),
                    "uploadedAt", java.time.Instant.now().toString()
            ));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Unable to store evidence"));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listEvidence() {
        try {
            Files.createDirectories(uploadDir);

            java.util.List<Map<String, Object>> evidenceList =
                    new java.util.ArrayList<>();

            try (var files = Files.list(uploadDir)) {
                files.filter(Files::isRegularFile)
                     .filter(path -> !path.getFileName().toString().endsWith(".meta"))
                     .forEach(path -> {
                         try {
                             String storedName = path.getFileName().toString();

                             Properties metadata = new Properties();
                             Path metadataPath =
                                     uploadDir.resolve(storedName + ".meta");

                             if (Files.exists(metadataPath)) {
                                 try (InputStream in =
                                              Files.newInputStream(metadataPath)) {
                                     metadata.load(in);
                                 }
                             }

                             Map<String, Object> item = new java.util.HashMap<>();
                             item.put("storedName", storedName);
                             item.put(
                                 "originalFilename",
                                 metadata.getProperty("originalFilename", storedName)
                             );
                             item.put("sizeBytes", Files.size(path));
                             item.put(
                                 "uploaded",
                                 Files.getLastModifiedTime(path)
                                      .toInstant().toString()
                             );

                             evidenceList.add(item);
                         } catch (IOException e) {
                             throw new java.io.UncheckedIOException(e);
                         }
                     });
            }

            return ResponseEntity.ok(evidenceList);

        } catch (IOException | java.io.UncheckedIOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Unable to list evidence"));
        }
    }

@GetMapping("/open/{storedName:.+}")
public ResponseEntity<Resource> openEvidence(
        @PathVariable String storedName,
        @RequestParam(defaultValue = "false") boolean download) {

    try {
        // Prevent directory traversal
        if (storedName.contains("/") || storedName.contains("\\")
                || storedName.contains("..")) {
            return ResponseEntity.badRequest().build();
        }                        
 
        Path filePath = uploadDir.resolve(storedName).normalize();

        if (!filePath.startsWith(uploadDir)
                || !Files.isRegularFile(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String disposition = download ? "attachment" : "inline";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    disposition + "; filename=\"" + storedName + "\""
                )
                .body(resource);

    } catch (IOException e) {
        return ResponseEntity.internalServerError().build();
    }
}


}
