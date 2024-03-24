package com.example.demo.service;

import com.example.demo.domain.ImageData;
import com.example.demo.exceptionHandling.*;
import com.example.demo.repository.ImageRepository;
import com.example.demo.util.KeyGeneratorAES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ImageService {

    private ImageRepository imageRepository;
    private AESEncryption aesEncryption;
    private FileConvert fileConvert;
    private ImageProcessor imageProcessor;

    private static final int MAX_WIDTH = 5000;
    private static final int MAX_HEIGHT = 5000;
    private static final String RESULT_ZIP = "result.zip";
    private static final String ATTACHMENT = "attachment";


    private static final List<String> NEW_UPLOADED_IMAGES_NAMES = new ArrayList<>();
    private static List<String> allImageNameInDB = new ArrayList<>();


    @Autowired
    public ImageService(ImageRepository imageRepository, AESEncryption aesEncryption, FileConvert fileConvert, ImageProcessor imageProcessor) {
        this.imageRepository = imageRepository;
        this.aesEncryption = aesEncryption;
        this.fileConvert = fileConvert;
        this.imageProcessor = imageProcessor;
    }


    public void uploadImages(List<MultipartFile> fileList, List<String> description) throws NoSuchAlgorithmException {
        if (fileList == null || fileList.isEmpty()) {
            throw new ZeroInputFileException();
        }
        allImageNameInDB = imageRepository.findAllName();

        if (allImageNameInDB.isEmpty()) {
            KeyGeneratorAES.init();
        }

        for (int i = 0; i < fileList.size(); i++) {
            try {
                uploadImage(fileList.get(i), description.get(i));
            } catch (RuntimeException e) {
                log.info(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        NEW_UPLOADED_IMAGES_NAMES.clear();
    }

    public void uploadImage(MultipartFile file, String description) throws IOException {

        String fileName = isValidFileName(file.getOriginalFilename());
        String contentType = isValidContentType(file.getContentType());

        isAlreadyExist(fileName);
        isValidExtension(contentType, fileName);

        BufferedImage imageSize = ImageIO.read(file.getInputStream());
        isSizeLarge(imageSize.getHeight(), imageSize.getWidth(), fileName);

        int[] widthXHeight;
        try {
            MultipartFile resizedMultipartFile = file;
            if (!(description == null || description.isEmpty())) {
                widthXHeight = isValidDescription(description);
                resizedMultipartFile = resizeImage(file, widthXHeight[1], widthXHeight[0]);
            }


            NEW_UPLOADED_IMAGES_NAMES.add(fileName);
            byte[] fileData = resizedMultipartFile.getBytes();
            byte[] encrypted = aesEncryption.encrypt(fileData);

            ImageData encryptedImageData = new ImageData(fileName, contentType, encrypted);

            imageRepository.save(encryptedImageData);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        } catch (ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }


    private MultipartFile resizeImage(MultipartFile file, int width, int height) throws IOException {
        File tempFile = File.createTempFile("converted", ".png");
        file.transferTo(tempFile);
        File resizedFile = imageProcessor.resizeImage(tempFile, width, height);
        MultipartFile resizedMultipartFile = fileConvert.convertTemptFileToMultipartFile(file, resizedFile);

        return resizedMultipartFile;
    }

    public ResponseEntity<byte[]> downloadAllImageInZIP() throws Exception {
        List<ImageData> imageData = imageRepository.findAll();
        if (imageData.isEmpty()) {
            throw new EmptyDatabaseException();
        }
        return zipImages(imageData, RESULT_ZIP);
    }

    public ResponseEntity<byte[]> zipImages(List<ImageData> imageDataList, String zipFileName) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            for (ImageData imageData : imageDataList) {
                byte[] fileData = imageData.getImageData();
                byte[] decrypted = aesEncryption.decrypt(fileData);

                ZipEntry zipEntry = new ZipEntry(imageData.getName());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(decrypted);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(ATTACHMENT, zipFileName);
        headers.setContentLength(byteArrayOutputStream.size());

        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    }

    public ImageData getFile(String fileName) throws Exception {
        Optional<ImageData> optionalImageData = imageRepository.findByName(fileName);
        if (optionalImageData.isPresent()) {
            ImageData returnImageData = optionalImageData.get();
            byte[] decrypted = aesEncryption.decrypt(returnImageData.getImageData());
            returnImageData.setImageData(decrypted);
            return returnImageData;
        } else {
            throw new ImageNotFoundException("Image not found with this name: " + fileName);
        }
    }


    private void isSizeLarge(int height, int width, String originalFilename) {
        if (height > MAX_HEIGHT || width > MAX_WIDTH) {
            throw new ImageTooLargeException("Image (" + originalFilename + ") too large! Max valid image size 5000x5000");
        }
    }

    private void isValidExtension(String contentType, String originalFilename) {
        if (!(contentType.contains("png") || contentType.contains("jpeg"))) {
            throw new ImageNotValidExtension("Image (" + originalFilename + ") is not PNG or JPG.");
        }
    }

    private void isAlreadyExist(String name) {
        if (allImageNameInDB.contains(name) || NEW_UPLOADED_IMAGES_NAMES.contains(name)) {
            throw new ImageAlreadyInDBException("Image (" + name + ") already in database");
        }
    }

    private String isValidFileName(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new FileNameIsEmptyException("Your image has no file name");
        }
        return originalFilename;
    }

    private String isValidContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            throw new ContentTypeEmptyException("Content type is null or Empty");
        }
        return contentType;
    }

    private int[] isValidDescription(String description) {


        String[] widthXHeightStr = description.split("x");
        if (widthXHeightStr.length != 2) {
            throw new InvalidDescriptionException("Description is not in the correct format");
        }

        int[] widthXHeight = new int[2];
        try {
            widthXHeight[0] = Integer.parseInt(widthXHeightStr[0]);
            widthXHeight[1] = Integer.parseInt(widthXHeightStr[1]);
        } catch (NumberFormatException e) {
            throw new InvalidDescriptionException("Description is not in the correct format, only digits are acceptable");
        }
        if (widthXHeight[0] > MAX_WIDTH || widthXHeight[1] > MAX_HEIGHT) {
            throw new TooLargeResizeParamException("Resizing is not possible, maximum number of pixels is " + MAX_WIDTH + "x" + MAX_HEIGHT);
        }
        return widthXHeight;
    }
}
