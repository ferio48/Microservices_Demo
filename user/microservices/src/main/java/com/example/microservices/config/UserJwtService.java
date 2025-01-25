package com.example.microservices.config;

import com.example.microservices.dto.ValidateRequest;
import com.example.microservices.response.BasicRestResponse;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserJwtService {
    @Autowired
    private RestTemplate restTemplate;
    public String authorizeRequest(String token) {
        if(token == null) throw new RuntimeException("NO TOKEN FOUND");
        System.out.println("token " + token);

        ValidateRequest validateRequest = ValidateRequest.builder().token(token).authority("admin:create").build();

        ResponseEntity<String> validateResponse = restTemplate.exchange(
                "http://role-based-security-service/api/v1/admin/validateToken",
                HttpMethod.POST,
                new HttpEntity<>(validateRequest),
                new ParameterizedTypeReference<String>() {});

        if(validateResponse.getBody() == null) throw new RuntimeException("VALIDATE TOKEN RESPONSE IS EMPTY");

        return validateResponse.getBody();
    }
}
