package com.example.microservices.service;

import com.example.microservices.controller.AbstractRestHandler;
import com.example.microservices.dto.UserRequest;
import com.example.microservices.dto.UserResponse;
import com.example.microservices.entity.User;
import com.example.microservices.exception.DBOperationException;
import com.example.microservices.exception.ResourceNotFoundException;
import com.example.microservices.repository.UserRepository;
import com.example.microservices.response.BasicRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public BasicRestResponse getUser(Integer id) {
        BasicRestResponse response = new BasicRestResponse();
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new ResourceNotFoundException("User with doesn't exist, id: " + id);
        }
        response.setContent(user.get());
        return response;
    }

    @Override
    public BasicRestResponse saveUser(UserRequest userRequest) {
        BasicRestResponse response = new BasicRestResponse();
        User user = User.builder()
                .name(userRequest.getName())
                .phone(userRequest.getPhone())
                .build();
        userRepository.save(user);
        response.setContent(user);
        return response;
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public BasicRestResponse updateUser(Integer userId, UserRequest userRequest) {
        BasicRestResponse res = new BasicRestResponse();
//        UserResponse response = UserResponse.builder().build();
//        response.setUserId(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User with the given UserId doesn't exist");
        }
        User user = optionalUser.get();
        if(userRequest.getName() != null) {
            user.setName(userRequest.getName());
//            response.setName(userRequest.getName());
        }
        if(userRequest.getPhone() != null) {
            user.setPhone(userRequest.getPhone());
//            response.setPhone(userRequest.getPhone());
        }
        userRepository.save(user);
        res.setContent(user);
        return res;
    }
}
