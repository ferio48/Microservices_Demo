package com.example.microservices.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public HttpEntity<MultiValueMap<String, Object>> makeFolderWithFirstname(MultipartFile file,
                                                                             String email,
                                                                             String token,
                                                                             String getUsernameResponse) throws IOException {
        String folderPath = "C:\\Users\\prana\\Documents\\" + getUsernameResponse + "\\";

        String filePath = folderPath + file.getOriginalFilename();

        Path destinationFolder = Paths.get(folderPath);

        if(!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

        HttpHeaders headers = new HttpHeaders();
        String bearerToken = "Bearer " + token;
        headers.set("Authorization", bearerToken);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add("email", email);
        body.add("folder", destinationFolder);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return requestEntity;
    }

    @Override
    public HttpEntity<MultiValueMap<String, Object>> makeFolderWithFirstnameAndDeleteFileName(MultipartFile file,
                                                                                              String email,
                                                                                              String token,
                                                                                              String getUsernameResponse,
                                                                                              String deleteFileName) throws IOException {
        String folderPath = "C:\\Users\\prana\\Documents\\" + getUsernameResponse + "\\";

        String filePath = folderPath + file.getOriginalFilename();

        Path destinationFolder = Paths.get(folderPath);

        if(!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add("email", email);
        body.add("token", token);
        body.add("folderpath", destinationFolder);
        body.add("filename", deleteFileName);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return requestEntity;

    }
}
