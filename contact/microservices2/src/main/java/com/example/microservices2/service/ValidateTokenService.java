package com.example.microservices2.service;


import com.example.microservices2.dto.ValidateRequest;

public interface ValidateTokenService {
    public String validateToken(ValidateRequest validateRequest);
}
