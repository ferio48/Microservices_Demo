package com.pranavsalaria.RoleBasedSecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendToContact implements Serializable {
    private List<Contact> contactList;
    private Integer userId;
    private String email;
    private String token;
}
