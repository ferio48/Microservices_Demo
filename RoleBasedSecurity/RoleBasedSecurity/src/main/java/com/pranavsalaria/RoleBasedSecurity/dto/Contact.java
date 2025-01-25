package com.pranavsalaria.RoleBasedSecurity.dto;

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
//    @JsonProperty("cId")
    private Integer cId;
    private String email;
    private String contactName;
    private Integer userId;
}
