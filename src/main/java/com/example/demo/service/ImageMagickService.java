package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class ImageMagickService implements ImageProcessor {
    @Override
    public File resizeImage(File file, int width, int height) {
        try {
            File output = File.createTempFile("converted", ".png");

            ConvertCmd cmd = new ConvertCmd();
            IMOperation op = new IMOperation();
            op.addImage(file.getAbsolutePath());
            op.resize(width, height);
            op.addImage(output.getAbsolutePath());
            cmd.run(op);

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("Image resizing failed");
        return file;
    }
}
