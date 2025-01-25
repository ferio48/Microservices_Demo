package com.example.microservices.service;

import com.example.microservices.dto.ValidateRequest;

public interface ValidateTokenService {
    public String validateToken(ValidateRequest validateRequest);

    public String getUserName(ValidateRequest validateRequest);
}
