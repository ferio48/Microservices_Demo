package com.example.microservices2.service;

import com.example.microservices2.dto.ContactRequest;
import com.example.microservices2.dto.ContactResponse;
import com.example.microservices2.dto.SendToContact;
import com.example.microservices2.entity.Contact;
import com.example.microservices2.repository.ContactRepository;
import com.example.microservices2.response.BasicRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ContactServiceIMPL implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public BasicRestResponse getContactsOfUser(Integer id) {
        BasicRestResponse response = new BasicRestResponse();
        response.setContent(contactRepository.findByUserId(id));
        return response;
//        return response;
    }

    @Override
    public List<Contact> saveContact(SendToContact sendToContact) {
        List<Contact> contacts = sendToContact.getContactList();
        contacts.forEach(contact -> contact.setUserId(sendToContact.getUserId()));
        contactRepository.saveAll(contacts);
//        System.out.println(contacts.stream().map(this::mapToContactResponse).collect(Collectors.toList()));
//        return contacts.stream().map(this::mapToContactResponse).collect(Collectors.toList());
        System.out.println(contacts);
        return contacts;
    }

    @Override
    public void deleteContact(Integer userId) {
        contactRepository.deleteAllByUserId(userId);
    }

    @Override
    public List<Contact> updateContact(Integer userId, List<Contact> contacts) {
        List<ContactResponse> response = new ArrayList<>();
        List<Contact> contactsFromDB = contactRepository.findAllByUserId(userId);
        List<Integer> idsFromUI = contacts.stream().filter(contact -> contact.getContactId() != null).map(contact -> contact.getContactId()).toList();
        List<Contact> toRemove = contactsFromDB.stream().filter(contact -> !idsFromUI.contains(contact.getContactId())).toList();
        contactsFromDB.removeIf(contact -> !idsFromUI.contains(contact.getContactId()));
        System.out.println("toRemove" + toRemove);
        System.out.println("contacts" + contacts);
        toRemove.forEach(contact -> {
            if(contactRepository.findById(contact.getContactId()).isPresent()) contactRepository.deleteById(contact.getContactId());
        });

        for(Contact contact: contacts) {
            Contact contact1 = null;
            if(contact.getContactId() == null) {
                contact1 = Contact.builder().build();
                contactsFromDB.add(contact1);
            } else {
                Optional<Contact> contact2 = contactRepository.findById(contact.getContactId());
                if(contact2.isPresent()) contact1 = contact2.get();
                else {
                    contact1 = Contact.builder().build();
                    contactsFromDB.add(contact1);
                }
                System.out.println("ELSE" + contact1);
            }

            if(contact.getContactName() != null) {
                contact1.setContactName(contact.getContactName());
            }

            if(contact.getEmail() != null) {
                contact1.setEmail(contact.getEmail());
            }

            contact1.setUserId(userId);
        }
        contactRepository.saveAll(contactsFromDB);
        response = contactsFromDB.stream().map(this::mapToContactResponse).collect(Collectors.toList());
        System.out.println("response" + response);
//        return response;
        return contactsFromDB;
    }

    private ContactResponse rmapToContactResponse(ContactRequest contact) {
        return ContactResponse.builder()
                .cId(contact.getCId())
                .email(contact.getEmail())
                .contactName(contact.getContactName())
                .userId(contact.getUserId())
                .build();
    }

    private ContactResponse mapToContactResponse(Contact contact) {
        System.out.println("mapToContactResponse" + contact.getContactId());
        return ContactResponse.builder()
                .cId(contact.getContactId())
                .email(contact.getEmail())
                .contactName(contact.getContactName())
                .userId(contact.getUserId())
                .build();
    }
}
