package com.example.microservices2.controller;

import com.example.microservices2.dto.*;
import com.example.microservices2.entity.Contact;
import com.example.microservices2.response.BasicRestResponse;
import com.example.microservices2.service.ContactService;
import com.example.microservices2.service.ValidateTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequestMapping("/contact")
@Tag(name = "ContactController", description = "APIs for contact microservices")
public class ContractController {
    @Autowired
    private ValidateTokenService validateTokenService;
    private final SimpleMessageConverter converter = new SimpleMessageConverter();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "/saveContact",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "create new contacts", description = "This is api to save contact in contact controller class in contact microservices")
    @ApiResponse(responseCode = "201", description = "new Contact created",useReturnTypeSchema = true)
    public List<Contact> saveContact(@RequestBody SendToContact sendToContact) {
        return contactService.saveContact(sendToContact);
    }

    @RequestMapping(value = "/getContactList",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public BasicRestResponse getContacts(@RequestBody UserRequest userRequest) {
        return contactService.getContactsOfUser(userRequest.getId());
    }

    @Transactional
    @RequestMapping(value = "/deleteContact/{UserId}",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public BasicRestResponse deleteContact(@PathVariable("UserId") Integer userId) {
        contactService.deleteContact(userId);
        BasicRestResponse response = new BasicRestResponse();
        response.setContent("Contact Deleted Successfully!!!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @RequestMapping(value = "/updateContact",
            method = RequestMethod.PUT,
            produces = {"application/json", "application/xml"})
    public BasicRestResponse updateContact(@RequestBody UserRequest userRequest){
        BasicRestResponse response = new BasicRestResponse();
        response.setContent(contactService.updateContact(userRequest.getId(), userRequest.getContactList()));
        return response;
    }
}
