package com.pranavsalaria.RoleBasedSecurity.response;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BasicRestResponse implements Serializable {
    private String message;
    private Integer status;
    private List<String> errorList;
    private Timestamp time;
    private Object content;

    public BasicRestResponse(final Integer status, String message, final Timestamp time) {
        super();
        this.status = status;
        this.message = message;
        this.time = time;
    }
}
