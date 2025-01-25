package com.example.microservices.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Contact implements Serializable {
    private Integer contactId;
    private String email;
    private String contactName;
    private Integer userId;
}
