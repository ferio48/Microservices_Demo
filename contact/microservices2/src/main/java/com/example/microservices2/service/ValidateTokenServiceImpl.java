package com.example.microservices2.service;

import com.example.microservices2.dto.ValidateRequest;
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
        String email = validateRequest.getEmail();
        String token = validateRequest.getToken();
        if(email.isEmpty()) throw new RuntimeException("EMAIL NOT FOUND");
        if(token.isEmpty()) throw new RuntimeException("TOKEN NOT FOUND");
        ResponseEntity<String> validateResponse = restTemplate.exchange(
                "http://role-based-security-service/api/v1/admin/validateToken",
                HttpMethod.POST,
                new HttpEntity<>(validateRequest),
                new ParameterizedTypeReference<String>() {});

        if(validateResponse.getBody() == null) throw new RuntimeException("VALIDATE TOKEN RESPONSE IS EMPTY");

        if(validateResponse.getBody().equals("TOKEN_INVALID")) throw  new RuntimeException("TOKEN IS INVALID");

        return validateResponse.getBody();
    }
}
