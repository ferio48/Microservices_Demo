package com.example.microservices2.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Contact API")
                        .description("API for contact microservices")
                        .version("1.0")
                        .contact(new Contact().name("Pranav Salaria")
                                .url("test@gmail.com")
                                .email("pranav212salaria@gmail.com"))
                        .license(new License().name("APACHE")))
                .externalDocs(new ExternalDocumentation().description("This is external documentation").url("externalDocs.com"));
    }
}
