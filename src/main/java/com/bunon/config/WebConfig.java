package com.bunon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir}")
    private String uploadDir;

    /**
     * Only PRODUCT images are public. NID documents live in uploads/nid and are NOT mapped here -
     * they are served through an admin-only controller method (VerificationController).
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path products = Paths.get(uploadDir, "products").toAbsolutePath().normalize();
        String location = products.toUri().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }
        registry.addResourceHandler("/uploads/products/**").addResourceLocations(location);
    }
}
