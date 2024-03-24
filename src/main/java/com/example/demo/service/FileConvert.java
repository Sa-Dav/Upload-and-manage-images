package com.example.demo.service;

import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileConvert {
    public MultipartFile convertTemptFileToMultipartFile(MultipartFile file, File tempFile) throws IOException {
        byte[] fileBytes = FileUtils.readFileToByteArray(tempFile);
        return new MockMultipartFile(
                file.getName(),
                file.getOriginalFilename(),
                file.getContentType(),
                fileBytes
        );
    }
}
