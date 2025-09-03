package com.bus.reservation.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bus Ticket Reservation API")
                        .version("v1")
                        .description("APIs for authentication, trips, booking, payments, and admin reports."))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Docs")
                        .url("/docs"));
    }
}
