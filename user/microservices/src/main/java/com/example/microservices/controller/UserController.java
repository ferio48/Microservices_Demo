package com.example.microservices.controller;

import com.example.microservices.dto.*;
import com.example.microservices.entity.Contact;
import com.example.microservices.entity.User;
import com.example.microservices.response.BasicRestResponse;
import com.example.microservices.service.FileService;
import com.example.microservices.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import com.example.microservices.service.ValidateTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private FileService fileService;
    @Autowired
    private ValidateTokenService validateTokenService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping(value = "/saveUser",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    public BasicRestResponse saveUser(@RequestBody UserRequest userRequest,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        BasicRestResponse res = new BasicRestResponse();
        User user = (User) userService.saveUser(userRequest).getContent();
        SendToContact sendToContact = SendToContact.builder()
                .contactList(userRequest.getContactList())
                .token(userRequest.getToken())
                .email(userRequest.getEmail())
                .userId(user.getUserId())
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);

        HttpEntity<SendToContact> httpEntity = new HttpEntity<>(sendToContact, headers);

        ResponseEntity<List<Contact>> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/saveContact",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<List<Contact>>() {});

        user.setContacts(responseEntity.getBody());
        res.setContent(user);
        res.setMessage("User with Contact saved Successfully");
        res.setStatus(HttpStatus.OK.value());
        return res;
    }

    @PostMapping("/getUserWithContacts")
    public BasicRestResponse getUser(@RequestBody UserRequest userRequest,
                                     @RequestHeader("Authorization") String bearerToken) {
         BasicRestResponse response = new BasicRestResponse();
         User user = (User) userService.getUser(userRequest.getId()).getContent();

         HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", bearerToken);

         HttpEntity<UserRequest> httpEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<BasicRestResponse> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/getContactList",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<BasicRestResponse>() {});

        user.setContacts((List<Contact>) responseEntity.getBody().getContent());
        response.setContent(user);
        response.setMessage("User fetched Successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @RequestMapping(value = "/deleteUser/{userId}",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(value = HttpStatus.OK)
    public BasicRestResponse deleteUser(@PathVariable Integer userId,
                                        @RequestHeader("Authorization") String bearerToken) {
        userService.deleteUser(userId);
        BasicRestResponse response = new BasicRestResponse();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);

        HttpEntity<Integer> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<BasicRestResponse> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/deleteContact/"+userId,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<BasicRestResponse>() {});

        response.setContent("User Deleted Successfully -- " + responseEntity.getBody().getContent());
        return response;
    }

    @RequestMapping(value = "/updateUser",
            method = RequestMethod.PUT,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public BasicRestResponse updateUser(@RequestBody UserRequest userRequest,
                                        @RequestHeader("Authorization") String bearerToken) {
        Integer userId = userRequest.getId();
        BasicRestResponse response = new BasicRestResponse();
        User user = (User) userService.updateUser(userId, userRequest).getContent();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);

        HttpEntity<UserRequest> httpEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<BasicRestResponse> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/updateContact",
                HttpMethod.PUT,
                httpEntity,
                new ParameterizedTypeReference<BasicRestResponse>() {});

        user.setContacts((List<Contact>) responseEntity.getBody().getContent());
        response.setContent(user);
        return response;
    }

    @RequestMapping(value = "/uploadFile",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public BasicRestResponse uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("email") String email,
                                        @RequestParam("token") String token) throws IOException {
        ValidateRequest validateRequest = ValidateRequest.builder().email(email).token(token).authority("admin:create").build();
        String getUsernameResponse = validateTokenService.getUserName(validateRequest);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = fileService.makeFolderWithFirstname(file, email, token, getUsernameResponse);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://file-upload-download-service/image/uploadImageToFilePath",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<String>() {});

        BasicRestResponse response = new BasicRestResponse();
        response.setContent(responseEntity.getBody());
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @RequestMapping(value = "/downloadFile",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    public ResponseEntity<?> downloadFile(@RequestParam("filename") String fileName,
                                        @RequestParam("email") String email,
                                        @RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("filename", fileName);
        body.add("email", email);
        body.add("token", token);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                "http://file-upload-download-service/image/downloadFile",
                HttpMethod.POST,
                requestEntity,
                byte[].class);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(responseEntity.getBody());
    }

    @RequestMapping(value = "/deleteFile",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    public BasicRestResponse deleteImage(@RequestParam("filename") String fileName,
                                         @RequestParam("email") String email,
                                         @RequestHeader("Authorization") String token) {
        String withoutBearer = token.substring(7);
        ValidateRequest validateRequest = ValidateRequest.builder().email(email).token(withoutBearer).authority("admin:create").build();
        String getUsernameResponse = validateTokenService.getUserName(validateRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("filename", fileName);
        body.add("username", getUsernameResponse);
        body.add("email", email);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://file-upload-download-service/image/deleteImageFromFilePath",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<String>() {});

        return BasicRestResponse.builder().content(responseEntity.getBody()).status(HttpStatus.OK.value()).build();
    }

    @RequestMapping(value = "/replaceFile",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public BasicRestResponse replaceFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("email") String email,
                                         @RequestParam("token") String token,
                                         @RequestParam("deletefileName") String deleteFileName) throws IOException {
        ValidateRequest validateRequest = ValidateRequest.builder().email(email).token(token).authority("admin:create").build();
        String getUsernameResponse = validateTokenService.getUserName(validateRequest);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = fileService.makeFolderWithFirstnameAndDeleteFileName(file, email, token, getUsernameResponse, deleteFileName);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://file-upload-download-service/image/updateImageFromFilePath",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<String>() {});

        BasicRestResponse response = new BasicRestResponse();
        response.setContent(responseEntity.getBody());
        response.setStatus(HttpStatus.OK.value());
        return response;
    }
}
