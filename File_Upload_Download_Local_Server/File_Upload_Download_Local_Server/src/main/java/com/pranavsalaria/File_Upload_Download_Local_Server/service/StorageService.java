package com.pranavsalaria.File_Upload_Download_Local_Server.service;

import com.pranavsalaria.File_Upload_Download_Local_Server.entity.FileData;
import com.pranavsalaria.File_Upload_Download_Local_Server.entity.ImageData;
import com.pranavsalaria.File_Upload_Download_Local_Server.repository.FileDataRepository;
import com.pranavsalaria.File_Upload_Download_Local_Server.repository.StorageRepository;
import com.pranavsalaria.File_Upload_Download_Local_Server.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private FileDataRepository dataRepository;
    public String uploadImage(MultipartFile file) throws IOException {
        System.out.println(file.getName() + " " + file.getOriginalFilename());
        ImageData imageData = storageRepository.save(ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());
        if(imageData != null) return "File upload successfully: " + file.getOriginalFilename();
        return "File not uploaded";
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = storageRepository.findByName(fileName);
        byte[] image = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return image;
    }

    public String uploadImageToFileSystem(String folderPath, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = folderPath + "/" + fileName;

        // Check if the destination folder exists, create it if not
        Path destinationFolder = Paths.get(folderPath);
        if (!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

//         Save file information to the database
        FileData fileData = dataRepository.save(FileData.builder()
                .name(fileName)
                .type(file.getContentType())
                .filePath(filePath).build());

        // Transfer the file to the destination folder
        Path destinationPath = destinationFolder.resolve(fileName);

        file.transferTo(destinationPath);

        if (fileData != null) {
            return "File uploaded successfully: " + filePath;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = dataRepository.findByName(fileName);

        String filePath = fileData.get().getFilePath();

        byte[] images = Files.readAllBytes(new File(filePath).toPath());

        return images;
    }

    public String deleteImageFromFileSystem(String userName, String fileName) throws IOException {
        String folderPath = "C:\\Users\\prana\\Documents\\" + userName;

        String filePath = folderPath + "/" + fileName;

        Path destinationFolder = Paths.get(folderPath);

        Path destinationPath = destinationFolder.resolve(fileName);

        if (Files.exists(destinationPath)) {
            Optional<FileData> optionalFileData = dataRepository.findByName(fileName);

            if(!optionalFileData.isPresent()) throw new RuntimeException("No FileData present");

            FileData fileData = optionalFileData.get();

            Files.delete(destinationPath);
        }
        dataRepository.deleteByName(fileName);

        if(!Files.exists(destinationPath)) return "File Deleted Successfully";
        return "File Delete Operation Failed";
    }

    public String replaceImageFromFileSystem(String destinationFolderPath, String deleteFileName, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = destinationFolderPath + "/" + fileName;
        String deleteFile = destinationFolderPath + "/" + deleteFileName;

        Path destinationFolder = Paths.get(destinationFolderPath);

        Path deleteDestinationPath = destinationFolder.resolve(deleteFile);

        if (Files.exists(deleteDestinationPath)) {
            dataRepository.deleteByName(deleteFileName);
            Files.delete(deleteDestinationPath);
        }

        FileData fileData = dataRepository.save(FileData.builder()
                .name(fileName)
                .type(file.getContentType())
                .filePath(filePath).build());

        Path destinationPath = destinationFolder.resolve(filePath);

        file.transferTo(destinationPath);

        if (fileData != null) {
            return "File uploaded successfully: " + filePath;
        }
        return "File Uploaded Failed";
    }
}
