# Image transformation and encryption - Backend

## Overview

This application provides functionalities to manage and process images. It includes features such as uploading images, resizing images, encrypting image data, downloading images in a zip format, and retrieving individual images.

## Getting Started

To use this application, ensure you have Java and Spring Boot installed on your system. Clone the repository and configure it in your preferred development environment.

## Features

### 1. Upload Images
   The uploadImages method allows users to upload multiple images along with descriptions. Uploaded images are processed to ensure they meet size and format requirements.

### 2. Image Resizing
   Images are resized to specified dimensions using the resizeImage method. This ensures uniformity and optimal display across various platforms.

### 3. Image Encryption
   Uploaded images are encrypted using AES encryption for secure storage and transmission. Encryption is performed before saving images to the database.

### 4. Download Images
   The downloadAllImageInZIP method enables users to download all images stored in the database as a zip file. Images are decrypted before packaging them into the zip file.

### 5. Retrieve Individual Images
   Individual images can be retrieved by their filenames using the getFile method. Retrieved images are decrypted before being served to the user.

## Usage

To use the Image Service, follow these steps:

1. Ensure all dependencies are installed and configured.
2. Initialize the application and connect it to your preferred database.
3. Use the provided methods to upload, resize, encrypt, download, and retrieve images as per your requirements.

## Methods
* uploadImages: Uploads a list of images with descriptions.
* uploadImage: Uploads a single image with a description.
* resizeImage: Resizes an image to the specified dimensions.
* downloadAllImageInZIP: Downloads all images as a ZIP archive.
* zipImages: Creates a ZIP archive containing the provided images.
* getFile: Retrieves an image file by name.
* Various private helper methods for validation and error handling.

## Endpoints

## 1. Upload Images Endpoint 
####   Method: POST
####   URL: /api/files
####   Description: Uploads images with optional descriptions and triggers resizing if required.
####   Request Parameters:
image: List of multipart files representing the images to be uploaded.
     description: List of descriptions corresponding to each image.
####   Response:
HTTP Status Code:201 Created if the images are successfully uploaded.

## 2. Download Image Endpoint
####    Method: GET
####   URL: /api/files/{fileName}
####   Description: Downloads a specific image by its name.
####  Path Parameter:
  fileName: Name of the image to be downloaded.
#### Response:
  Image data with appropriate content type.
## 3. Download All Images Endpoint
####   Method: GET
####   URL: /api/files
####   Description: Downloads all images stored in the database as a ZIP archive.
####  Response:
   ZIP file containing all images.

## Dependencies
   This application relies on the following dependencies:

* Spring Boot
* PostgreSQL Driver
* Lombok
* im4java
* Thumbnailator

## Test


### The easiest way to test is through the Postman application.
#### For a POST request to the "/api/files" endpoint:
You need to attach Key-Value pairs of "form-data" type to the Body
* For image resizing, attach Key-Value pairs with "image" as the key and the image file as the value. Also, include "description" with width and height parameters (e.g., "500x500", "1000x1000").
* If no resizing is needed, still attach "description" with an empty value for encrypted saving.

#### For a POST request to the "/api/files" and "/api/files/{fileName}" endpoint:
* In other cases, simply send the endpoint within a GET request frame, and upon decryption, you'll receive the resized image.

## Conditions:

* Each image must have a corresponding "description."
* Images cannot exceed 5000x5000 pixels in size.
* After resizing, images must still not exceed 5000x5000 pixels.
* Only upload images in the correct formats (PNG, JPG).
* Cannot upload two files with the same name.

## Set up PostgreSQL with Docker
$ docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=test1234 -d postgres

## Contact:
email: sarkdave8@gmail.com