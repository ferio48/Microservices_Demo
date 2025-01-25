package com.example.microservices.service;

import com.example.microservices.dto.UserRequest;
import com.example.microservices.dto.UserResponse;
import com.example.microservices.entity.User;
import com.example.microservices.response.BasicRestResponse;


public interface UserService {
    BasicRestResponse getUser(Integer id);

    BasicRestResponse saveUser(UserRequest userRequest);

    void deleteUser(Integer userId);

    BasicRestResponse updateUser(Integer userId, UserRequest userRequest);
}
