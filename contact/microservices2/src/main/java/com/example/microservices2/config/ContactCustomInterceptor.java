package com.example.microservices2.config;

import com.example.microservices2.dto.ValidateRequest;
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

public class ContactCustomInterceptor implements HandlerInterceptor {
    @Autowired
    private RestTemplate restTemplate;
    public ContactCustomInterceptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url  = request.getRequestURL().toString();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            ValidateRequest validateRequest = ValidateRequest.builder().token(jwt).build();
            if (url.contains("/saveContact") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                return validateResponse.equals("TOKEN_VALID");
            }
            else if (url.contains("/getContactList") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                return validateResponse.equals("TOKEN_VALID");
            }
            else if (url.contains("/deleteContact") && method.equals("POST")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                return validateResponse.equals("TOKEN_VALID");
            }
            else if (url.contains("/updateContact") && method.equals("PUT")) {
                validateRequest.setAuthority("admin:create");
                String validateResponse = callValidateResponse(validateRequest);
                return validateResponse.equals("TOKEN_VALID");
            }
        }
        new ObjectMapper().writeValue(response.getOutputStream(), "Authorization Failed!!!");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String callValidateResponse(ValidateRequest validateRequest) {
        ResponseEntity<String> validateResponse = restTemplate.exchange(
                "http://localhost:8089/api/v1/admin/validateToken",
//                "http://role-based-security-service/api/v1/admin/validateToken",
                HttpMethod.POST,
                new HttpEntity<>(validateRequest),
                new ParameterizedTypeReference<String>() {});

        if(validateResponse.getBody() == null) throw new RuntimeException("VALIDATE TOKEN RESPONSE IS EMPTY");

        if(validateResponse.getBody().equals("TOKEN_INVALID")) throw  new RuntimeException("TOKEN IS INVALID");

        return validateResponse.getBody();
    }
}
