package com.pranavsalaria.RoleBasedSecurity.controller;

import com.pranavsalaria.RoleBasedSecurity.dto.Contact;
import com.pranavsalaria.RoleBasedSecurity.dto.SendToContact;
import com.pranavsalaria.RoleBasedSecurity.dto.UserRequest;
import com.pranavsalaria.RoleBasedSecurity.response.BasicRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {
    @Autowired
    private RestTemplate restTemplate;
//    @Operation(
//            description = "Get endpoint for manager",
//            summary = "This is a summary for management get endpoint",
//            responses = {
//                    @ApiResponse(
//                            description = "Success",
//                            responseCode = "200"
//                    ),
//                    @ApiResponse(
//                            description = "Unauthorized / Invalid Token",
//                            responseCode = "403"
//                    )
//            }
//
//    )
    @GetMapping("/getContact/{userId}")
    @PreAuthorize("hasAuthority('management:read')")
    public BasicRestResponse get(@PathVariable("userId") Integer id) {
        BasicRestResponse response = new BasicRestResponse();
        ResponseEntity<BasicRestResponse> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/user/"+id,
                HttpMethod.GET,
                new HttpEntity<>(id),
                new ParameterizedTypeReference<BasicRestResponse>() {});
        return responseEntity.getBody();
    }
    @RequestMapping(value = "/saveContact",
            method = RequestMethod.POST,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('management:create')")
    public List<Contact> saveContact(@RequestBody SendToContact sendToContact) {
        ResponseEntity<List<Contact>> responseEntity = restTemplate.exchange(
//                "http://localhost:8083/contact/saveContact",
                "http://contact-service/contact/saveContact",
                HttpMethod.POST,
                new HttpEntity<>(sendToContact),
                new ParameterizedTypeReference<List<Contact>>() {});
        return responseEntity.getBody();
    }
    @PutMapping("/updateContact")
    public BasicRestResponse updateContact(@RequestBody UserRequest userRequest) {
        BasicRestResponse response = new BasicRestResponse();
        Integer userId = userRequest.getId();
        ResponseEntity<List<Contact>> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/updateContact/"+userId,
                HttpMethod.PUT,
                new HttpEntity<>(userRequest.getContactList()),
                new ParameterizedTypeReference<List<Contact>>() {});
        response.setMessage("Contact updated Successfully");
        response.setContent(responseEntity.getBody());
        return response;
    }
    @DeleteMapping("/deleteContacts/{userId}")
    @PreAuthorize("hasAuthority('management:delete')")
    public BasicRestResponse delete(@PathVariable("userId") Integer id) {
        BasicRestResponse response = new BasicRestResponse();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://contact-service/contact/deleteContact/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(id),
                new ParameterizedTypeReference<String>() {});
        response.setMessage("Contacts Deleted Successfully");
        return response;
    }
}