package com.example.demo.controller;

import com.example.demo.domain.ImageData;
import com.example.demo.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@Slf4j
public class ImageController {
    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping()
    @Operation(summary = "Upload images with resize parameter")
    public ResponseEntity<Void> saveImages(@RequestParam("image") List<MultipartFile> multipartFile, @RequestParam("description") List<String> description) throws Exception {
        imageService.uploadImages(multipartFile, description);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{fileName}")
    @Operation(summary = "Download image by its name")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws Exception {
        ImageData a = imageService.getFile(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(a.getImageData());
    }

    @GetMapping()
    @Operation(summary = "Download all images")
    public ResponseEntity<?> downloadZIP() throws Exception {
        ResponseEntity<byte[]> zip = imageService.downloadAllImageInZIP();

        return zip;
    }
}
