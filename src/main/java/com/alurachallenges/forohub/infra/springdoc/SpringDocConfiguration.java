package com.alurachallenges.forohub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                )
                .info(new Info()
                        .title("Foro Hub CHallenge Alura Latam - Oracle ONE")
                        .description("La siguiente aplicación es la creación de un API REST de un Foro entre donde las personas " +
                                "pueden realizar aportes, comentarios de diversos temas. Este foro esta desarrollado en base a " +
                                "un comunidad educativa para que los estudiantes puedan compartir y resolver sus dudas.")
                        .contact(new Contact()
                                .name("Estudiante Backend Juan")
                                .email("juanpapa501@hotmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                ));
    }
}
