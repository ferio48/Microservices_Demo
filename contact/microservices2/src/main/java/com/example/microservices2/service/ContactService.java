package com.example.microservices2.service;

import com.example.microservices2.dto.ContactRequest;
import com.example.microservices2.dto.ContactResponse;
import com.example.microservices2.dto.SendToContact;
import com.example.microservices2.entity.Contact;
import com.example.microservices2.response.BasicRestResponse;

import java.util.List;

public interface ContactService {
    public BasicRestResponse getContactsOfUser(Integer id);

    List<Contact> saveContact(SendToContact sendToContact);

    void deleteContact(Integer userId);

    List<Contact> updateContact(Integer userId, List<Contact> contacts);
}
