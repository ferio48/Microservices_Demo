package com.example.microservices2.dto;

import com.example.microservices2.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String token;
    private Integer id;
    private String name;
    private String phone;
    private List<Contact> contactList;
}

