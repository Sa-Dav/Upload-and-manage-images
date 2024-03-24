package com.example.demo.service;

import java.io.File;
import java.io.IOException;

public interface ImageProcessor {
    File resizeImage(File file, int width, int height) throws IOException;
}
