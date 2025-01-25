package com.example.microservices2.dto;

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
