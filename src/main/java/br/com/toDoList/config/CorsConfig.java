package br.com.toDoList.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{

    public void addCorsMappings(@NonNull CorsRegistry registry){
        registry.addMapping("/**")
        .allowedOriginPatterns("http://localhost:3000") // URL do React
        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
        .allowCredentials(true);
    }
}
