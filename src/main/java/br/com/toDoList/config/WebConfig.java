package br.com.toDoList.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override 
    // Serve como arquivos estáticos, o código só adiciona o nome 
    // da foto do banco com o caminho informado pelo backend
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/");
    }
}
