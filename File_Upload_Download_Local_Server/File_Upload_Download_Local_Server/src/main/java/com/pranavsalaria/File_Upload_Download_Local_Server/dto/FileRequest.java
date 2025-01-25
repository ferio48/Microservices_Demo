package com.pranavsalaria.File_Upload_Download_Local_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest implements Serializable {
    private String email;
    private String token;
    private MultipartFile file;
}
