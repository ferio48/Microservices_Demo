package com.pranavsalaria.RoleBasedSecurity.service;

import com.netflix.discovery.converters.Auto;
import com.pranavsalaria.RoleBasedSecurity.config.JwtService;
import com.pranavsalaria.RoleBasedSecurity.dao.UserRepository;
import com.pranavsalaria.RoleBasedSecurity.dto.ValidateRequest;
import com.pranavsalaria.RoleBasedSecurity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidateService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    public String validateToken(ValidateRequest validateRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(validateRequest.getToken()));
        System.out.println(userDetails.getAuthorities());
        if(jwtService.isTokenValid(validateRequest.getToken(), userDetails)) {
            List<String> convertedAuthorities = userDetails.getAuthorities().stream().map(Object::toString).toList();
            System.out.println("convertedAuthorities: " + convertedAuthorities);
            if(convertedAuthorities.stream().anyMatch(authority -> authority.equals(validateRequest.getAuthority()))) {
                return "TOKEN_VALID";
            }
        }
        return "TOKEN_INVALID";
    }

    public String getUserName(ValidateRequest validateRequest) {
        String email = jwtService.extractUsername(validateRequest.getToken());
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()) throw  new RuntimeException("USER NOT FOUND");
        return optionalUser.get().getFirstname();
    }
}
