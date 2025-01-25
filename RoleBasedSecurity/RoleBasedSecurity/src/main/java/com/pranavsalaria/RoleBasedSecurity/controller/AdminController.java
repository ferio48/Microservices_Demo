package com.pranavsalaria.RoleBasedSecurity.controller;

import com.netflix.discovery.converters.Auto;
import com.pranavsalaria.RoleBasedSecurity.config.JwtService;
import com.pranavsalaria.RoleBasedSecurity.dao.UserRepository;
import com.pranavsalaria.RoleBasedSecurity.dto.*;
import com.pranavsalaria.RoleBasedSecurity.response.BasicRestResponse;
import com.pranavsalaria.RoleBasedSecurity.service.ValidateService;
import com.pranavsalaria.RoleBasedSecurity.user.User;
import jakarta.ws.rs.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private ValidateService validateService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService service;
    @Autowired
    private RestTemplate restTemplate;
    @PostMapping("/validateToken")
    public String validateToken(@RequestBody ValidateRequest validateRequest) {
        return validateService.validateToken(validateRequest);
    }
    @PostMapping("/getUserNameByToken")
    public String getUserNameByToken(@RequestBody ValidateRequest validateRequest) {
        return validateService.getUserName(validateRequest);

    }

    @GetMapping("/getUser")
    @PreAuthorize("hasAuthority('admin:read')")
    public BasicRestResponse get(@RequestBody UserRequest userRequest) {
        BasicRestResponse response = new BasicRestResponse();
        ResponseEntity<BasicRestResponse> responseEntity = restTemplate.exchange(
                "http://user-service/user/getUserWithContacts",
                HttpMethod.POST,
                new HttpEntity<>(userRequest),
                new ParameterizedTypeReference<BasicRestResponse>() {});
        return responseEntity.getBody();
    }
    @RequestMapping(value = "/saveUser",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('admin:create')")
//    @PreAuthorize("hasAuthority('admin:create') || hasAuthority('management:create')")
    public BasicRestResponse post(@RequestBody UserRequest userRequest) {
        try {
            BasicRestResponse res = new BasicRestResponse();
            ResponseEntity<BasicRestResponse> responseEntityUser = restTemplate.exchange(
                    "http://user-service/user/saveUser",
                    HttpMethod.POST,
                    new HttpEntity<>(userRequest),
                    new ParameterizedTypeReference<BasicRestResponse>() {});

            System.out.println(responseEntityUser.getBody());
            res.setContent(responseEntityUser.getBody());
            res.setMessage("User with Contact saved Successfully");
            res.setStatus(HttpStatus.OK.value());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Hostname resolution failed. Check DNS configuration and host availability.");
        }
        return null;
    }
    @PutMapping("/updateUser")
    @PreAuthorize("hasAuthority('admin:update')")
    public BasicRestResponse put(
//            @PathVariable("userId") Integer userId,
            @RequestBody UserRequest userRequest) {
        BasicRestResponse response = new BasicRestResponse();
        ResponseEntity<BasicRestResponse> responseEntity = restTemplate.exchange(
                "http://user-service/user/updateUser",
                HttpMethod.PUT,
                new HttpEntity<>(userRequest),
                new ParameterizedTypeReference<BasicRestResponse>() {});
        response.setMessage("User update Successfully");
        response.setContent(responseEntity.getBody());
        return response;
    }
    @DeleteMapping("/deleteUser/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public BasicRestResponse delete(@PathVariable("userId") Integer id) {
        BasicRestResponse response = new BasicRestResponse();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://user-service/user/deleteUser/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(id),
                new ParameterizedTypeReference<String>() {});
        response.setMessage("User with Contacts Deleted Successfully");
        return response;
    }
}
