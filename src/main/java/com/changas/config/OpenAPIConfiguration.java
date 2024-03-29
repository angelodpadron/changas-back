package com.changas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("dev")
@Configuration
public class OpenAPIConfiguration {

    @Value("${app.base-url}")
    private String serverUrl;
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl(this.serverUrl);

        Contact contact = new Contact();
        contact.setName("Changas Support Team");
        contact.setEmail("support@changas.com");

        Info info = new Info()
                .title("Changas API")
                .version("0.1")
                .description("This API exposes the main endpoints of Changas")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
