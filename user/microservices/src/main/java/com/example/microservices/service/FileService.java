package com.example.microservices.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    HttpEntity<MultiValueMap<String, Object>> makeFolderWithFirstname(MultipartFile file, String email, String token, String getUsernameResponse) throws IOException;

    HttpEntity<MultiValueMap<String, Object>> makeFolderWithFirstnameAndDeleteFileName(MultipartFile file, String email, String token, String getUsernameResponse, String deleteFileName) throws IOException;
}
