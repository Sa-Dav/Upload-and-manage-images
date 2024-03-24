package com.example.demo.config;

import com.example.demo.service.ImageMagickService;
import com.example.demo.service.ImageProcessor;
import com.example.demo.service.ThumbnailatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ImageProcessorConfig {
    @Bean("thumbnailatorService")
    @Primary
    public ImageProcessor thumbnailatorService() {
        return new ThumbnailatorService();
    }

    @Bean("imageMagickService")
    public ImageProcessor imageMagickService() {
        return new ImageMagickService();
    }
}