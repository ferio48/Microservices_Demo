package com.pranavsalaria.File_Upload_Download_Local_Server;

import com.pranavsalaria.File_Upload_Download_Local_Server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

@SpringBootApplication
@EnableWebMvc
public class FileUploadDownloadLocalServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadDownloadLocalServerApplication.class, args);
	}

}
