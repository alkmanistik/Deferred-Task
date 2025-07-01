package com.alkmanistik.deferred_thread.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Deferred Task Processor API",
                description = "API системы отложенных задач",
                version = "1.0.0",
                contact = @Contact(
                        name = "Alkmanistik",
                        email = "erik.fattakhov.04@mail.ru",
                        url = "https://alkmanistik.github.io/"
                )
        )
)
public class OpenApiConfig {
    // Конфигурация для Swagger
}