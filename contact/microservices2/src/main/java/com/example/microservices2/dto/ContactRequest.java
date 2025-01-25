package com.example.microservices2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
//@EqualsAndHashCode(exclude = "cId")
@NoArgsConstructor
@Schema(name = "Contact Request")
public class ContactRequest implements Serializable {
//    @JsonProperty("cId")
    @Schema(name = "contact_id", description = "Contact ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer cId;
    private String email;
    private String contactName;
    private Integer userId;
}
