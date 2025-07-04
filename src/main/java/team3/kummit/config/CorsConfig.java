package team3.kummit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() { //개발 cors
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://144.24.81.195:5173", "http://localhost:5173", "http://144.24.81.195")
                        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH", "HEAD")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }
}
