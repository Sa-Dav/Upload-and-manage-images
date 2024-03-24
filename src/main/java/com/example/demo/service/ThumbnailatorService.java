package com.example.demo.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ThumbnailatorService implements ImageProcessor {

    @Override
    public File resizeImage(File file, int width, int height) throws IOException {
        File output = File.createTempFile("converted", ".png");
        Thumbnails.of(file).forceSize(width, height).toFile(output);
        return output;
    }
}
