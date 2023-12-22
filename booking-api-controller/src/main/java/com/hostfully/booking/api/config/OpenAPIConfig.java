package com.hostfully.booking.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenAPIConfig {

    @Value("${open-api.dev-url}")
    private String devUrl;

    @Value("${open-api.prd-url}")
    private String prdUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Example of Server URL for DEV Environment");

        Server prodServer = new Server();
        prodServer.setUrl(prdUrl);
        prodServer.setDescription("Example of Server URL for PRD Environment");

        Contact contact = new Contact();
        contact.setEmail("comp.vinicius@gmail.com");
        contact.setName("Vinicius Figueiredo Nogueira");
        contact.setUrl("https://github.com/viniciusnogueira14");

        Info info = new Info()
                .title("API for evaluation in the Code Interview.")
                .version("1.0")
                .contact(contact)
                .description("This API is a project created for evaluation in the Code Interview.");

        return new OpenAPI().info(info).servers(Arrays.asList(devServer, prodServer));
    }
}
