package com.snowmanvillage.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origin-dev}")
    private String allowedOriginDev;

    @Value("${cors.allowed-origin-deploy}")
    private String allowedOriginDeploy;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOriginDev, allowedOriginDeploy)
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE");
    }

}
