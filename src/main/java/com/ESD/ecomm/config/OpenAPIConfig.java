package com.ESD.ecomm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce API")
                        .version("1.0.0")
                        .description("""
                                A comprehensive Spring Boot E-Commerce REST API with JWT Authentication.
                                
                                ## Features
                                - **User Management**: Registration, login, profile management
                                - **Product Catalog**: Browse, search, and manage products
                                - **Shopping Cart**: Add/remove items, manage cart contents
                                - **Order Management**: Place orders, track status, manage order items
                                - **Payment Processing**: Handle payment transactions
                                - **Review System**: Product reviews and ratings
                                - **Wishlist**: Save favorite products
                                - **Address Management**: Multiple shipping addresses
                                - **Category Management**: Organize products by categories
                                - **Shipment Tracking**: Monitor order deliveries
                                
                                ## Authentication
                                This API uses JWT (JSON Web Token) for authentication. To use protected endpoints:
                                1. Register a new account using `/api/auth/register`
                                2. Login using `/api/auth/login` to get your JWT token
                                3. Use the token in the Authorization header: `Bearer <your-token>`
                                4. Click the 🔒 icon next to any secured endpoint to enter your token
                                
                                ## Error Handling
                                The API returns standard HTTP status codes and detailed error messages in JSON format.
                                """)
                        .termsOfService("https://esdcommerce.com/terms")
                        .contact(new Contact()
                                .name("ESD Development Team")
                                .email("support@esdcommerce.com")
                                .url("https://github.com/esd-team")
                        )
                        .license(new License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server - Local Environment"),
                        new Server()
                                .url("https://api.esdcommerce.com")
                                .description("Production Server - Live Environment")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token here (without 'Bearer' prefix)")
                                        .name("Authorization")
                        )
                )
                .tags(List.of(
                        new Tag()
                                .name("Authentication")
                                .description("User authentication and registration endpoints"),
                        new Tag()
                                .name("User Management")
                                .description("User profile and account management"),
                        new Tag()
                                .name("Product Management")
                                .description("Product catalog and inventory management"),
                        new Tag()
                                .name("Category Management")
                                .description("Product category organization"),
                        new Tag()
                                .name("Shopping Cart")
                                .description("Shopping cart and cart items management"),
                        new Tag()
                                .name("Order Management")
                                .description("Order processing and tracking"),
                        new Tag()
                                .name("Payment Processing")
                                .description("Payment transactions and billing"),
                        new Tag()
                                .name("Reviews & Ratings")
                                .description("Product reviews and customer feedback"),
                        new Tag()
                                .name("Wishlist")
                                .description("Customer wishlist management"),
                        new Tag()
                                .name("Address Management")
                                .description("Customer shipping addresses"),
                        new Tag()
                                .name("Shipment Tracking")
                                .description("Order fulfillment and delivery tracking")
                ));
    }
}