package com.pranavsalaria.RoleBasedSecurity.dto;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class MicUser implements Serializable{
        private Integer userId;
        private String name;
        private String phone;
        private List<Contact> contacts;
}
