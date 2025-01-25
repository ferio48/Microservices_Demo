package com.pranavsalaria.RoleBasedSecurity.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidateRequest {
    private String email;
    private String token;
    private String authority;
}
