package com.example.microservices.config;

import com.example.microservices.dto.UserRequest;
import com.example.microservices.dto.ValidateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserCustomInterceptor implements HandlerInterceptor {
    @Autowired
    private RestTemplate restTemplate;
    public UserCustomInterceptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url  = request.getRequestURL().toString();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");
        boolean flag = false;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            ValidateRequest validateRequest = ValidateRequest.builder().token(jwt).build();
            if(url.contains("/saveUser") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/getUserWithContacts") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/deleteUser") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/updateUser") && method.equals("PUT")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/uploadFile") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/downloadFile") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/deleteFile") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
            else if(url.contains("/replaceFile") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                if(validateResponse.equals("TOKEN_VALID")) flag = true;
            }
        }
        if(!flag) {
            new ObjectMapper().writeValue(response.getOutputStream(), "Authorization Failed!!!");
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(response.getOutputStream().toString());

        UserRequest userRequest;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String callValidateResponse(ValidateRequest validateRequest) {
        ResponseEntity<String> validateResponse = restTemplate.exchange(
                "http://localhost:8089/api/v1/admin/validateToken",
                HttpMethod.POST,
                new HttpEntity<>(validateRequest),
                new ParameterizedTypeReference<String>() {});

        if(validateResponse.getBody() == null) throw new RuntimeException("VALIDATE TOKEN RESPONSE IS EMPTY");

        if(validateResponse.getBody().equals("TOKEN_INVALID")) throw  new RuntimeException("TOKEN IS INVALID");

        return validateResponse.getBody();
    }
}

