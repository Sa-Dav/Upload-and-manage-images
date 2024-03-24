package com.example.demo.controller;

import com.example.demo.domain.ImageData;
import com.example.demo.exceptionHandling.FileNameIsEmptyException;
import com.example.demo.exceptionHandling.ZeroInputFileException;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ImageControllerIT {
    @Mock
    private MockMultipartFile mockFile;

    @Autowired
    private ImageService imageService;

    @Test
    public void testUploadImagesWithNoFiles() {
        List<MultipartFile> fileList = new ArrayList<>();
        List<String> description = new ArrayList<>();
        assertThrows(ZeroInputFileException.class, () -> imageService.uploadImages(fileList, description));
    }

    @Test
    public void testUploadImagewithWrongFile()  {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        String description = "100x100";

        assertThrows(RuntimeException.class, () -> imageService.uploadImage(file, description));
    }

    @Test
    public void testUploadImageWithWrongName()  {
        when(mockFile.getOriginalFilename()).thenReturn("");
        String description = "100x100";

        assertThrows(FileNameIsEmptyException.class, () -> imageService.uploadImage(mockFile, description));
    }
}
