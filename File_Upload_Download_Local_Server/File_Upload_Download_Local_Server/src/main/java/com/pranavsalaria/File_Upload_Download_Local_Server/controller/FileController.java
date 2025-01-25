package com.pranavsalaria.File_Upload_Download_Local_Server.controller;

import com.pranavsalaria.File_Upload_Download_Local_Server.dto.ValidateRequest;
import com.pranavsalaria.File_Upload_Download_Local_Server.service.StorageService;
import com.pranavsalaria.File_Upload_Download_Local_Server.service.ValidateTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
@RestController
@RequestMapping("/image")
public class FileController {
    @Autowired
    private ValidateTokenService validateTokenService;
    @Autowired
    private StorageService service;

    @PostMapping("/uploadImageFromServer")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("email") String email,
                                         @RequestParam("token") String token) throws IOException {
        ValidateRequest validateRequest = ValidateRequest.builder().email(email).token(token).authority("admin:create").build();

        String validateResponse = validateTokenService.validateToken(validateRequest);

        if(!validateResponse.equals("TOKEN_VALID")) throw new RuntimeException("TOKEN IS STILL NOT VALID");

        String uploadImage = service.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=service.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

    @PostMapping("/uploadImageToFilePath")
    public ResponseEntity<String> uploadImageToFilePath(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("email") String email,
                                                        @RequestParam("folder") String destinationFolder) throws IOException {
        String folder = destinationFolder.substring(9,destinationFolder.length()-2);
        String uploadImage = service.uploadImageToFileSystem(folder, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @PostMapping("/downloadFile")
    public ResponseEntity<?> downloadImageFromFilePath(@RequestParam("filename") String fileName,
                                                       @RequestParam("email") String email) throws IOException {
        byte[] imageData=service.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

    @PostMapping("/deleteImageFromFilePath")
    public String deleteImageFromFilePath(@RequestParam("filename") String fileName,
                                          @RequestParam("username") String userName,
                                          @RequestParam("email") String email) throws IOException {
        return service.deleteImageFromFileSystem(userName, fileName);
    }

    @PostMapping("/updateImageFromFilePath")
    public String updateImageToFilePath(@RequestParam("image") MultipartFile file,
                                                   @RequestParam("folderpath") String folderPath,
                                                   @RequestParam("filename") String deleteFileName,
                                                   @RequestParam("email") String email) throws IOException {
        String folder = folderPath.substring(9,folderPath.length()-2);

        return service.replaceImageFromFileSystem(folder, deleteFileName, file);
    }
}
