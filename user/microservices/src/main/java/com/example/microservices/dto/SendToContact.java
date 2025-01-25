package com.example.microservices.dto;

import com.example.microservices.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendToContact implements Serializable {
    private List<Contact> contactList;
    private String token;
    private String email;
    private Integer userId;
}
