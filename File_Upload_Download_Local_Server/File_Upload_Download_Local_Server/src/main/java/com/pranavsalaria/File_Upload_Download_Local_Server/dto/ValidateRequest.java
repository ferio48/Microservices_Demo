package com.pranavsalaria.File_Upload_Download_Local_Server.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidateRequest {
    private String email;
    private String token;
    private String authority;
}
