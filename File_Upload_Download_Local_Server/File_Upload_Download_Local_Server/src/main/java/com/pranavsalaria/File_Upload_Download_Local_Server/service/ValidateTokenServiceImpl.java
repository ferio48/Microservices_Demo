package com.pranavsalaria.File_Upload_Download_Local_Server.service;

import com.pranavsalaria.File_Upload_Download_Local_Server.dto.ValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidateTokenServiceImpl implements ValidateTokenService{
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public String validateToken(ValidateRequest validateRequest) {
        String token  = validateRequest.getToken();
        String email = validateRequest.getEmail();
        if(token == null) throw new RuntimeException("NO TOKEN FOUND");
        if(email.isEmpty()) throw new RuntimeException("NO EMAIL FOUND");
        System.out.println("token & email: " + token + "--" + email);

        ResponseEntity<String> validateResponse = restTemplate.exchange(
                "http://role-based-security-service/api/v1/admin/validateToken",
                HttpMethod.POST,
                new HttpEntity<>(validateRequest),
                new ParameterizedTypeReference<String>() {});

        if(validateResponse.getBody() == null) throw new RuntimeException("VALIDATE TOKEN RESPONSE IS EMPTY");

        if(validateResponse.getBody().equals("TOKEN_INVALID")) throw  new RuntimeException("TOKEN IS INVALID");

        return validateResponse.getBody();
    }

    @Override
    public String getUserName(ValidateRequest validateRequest) {
        String token  = validateRequest.getToken();
        String email = validateRequest.getEmail();
        if(token == null) throw new RuntimeException("NO TOKEN FOUND");
        if(email.isEmpty()) throw new RuntimeException("NO EMAIL FOUND");
        System.out.println("token & email: " + token + "--" + email);

        ResponseEntity<String> getUsernameResponse = restTemplate.exchange(
                "http://role-based-security-service/api/v1/admin/validateToken",
                HttpMethod.POST,
                new HttpEntity<>(validateRequest),
                new ParameterizedTypeReference<String>() {});

        return getUsernameResponse.getBody();
    }
}
