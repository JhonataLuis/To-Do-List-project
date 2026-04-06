package br.com.toDoList.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    public JwtAuthenticationFilter jwtFilter;

    @Bean 
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .cors(cors -> cors.configurationSource(corsSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/uploads/**").permitAll()
            
                // Garante que os endpoints de User também precisem de token
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/tasks/**", "/api/tarefas/**").authenticated() // Protege as tarefas
                .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsSource(){
        CorsConfiguration config = new CorsConfiguration();

        //config.setAllowedOriginPatterns(List.of("*")); // Permite qualquer origem

        // Configuração para liberar acesso ao backend pelo celular (App React Native)
        /*config.setAllowedOrigins(List.of(
            "http://192.168.5.111", // Ip do celular (My)
            "http://192.168.5.111:8081" // Porta padrão do Metro Bundler
        ));*/
        // Permite as origens do Front-end
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:3000", 
            "http://localhost:8080", 
            "http://localhost:5173",
            "http://192.168.5.111:8081",
            "http://192.168.5.111",
            "http://localhost:[*]",
            "https://*.expo.proxy.io",
            "https://*.ngrok-free.app",
            "exp://[*]"
           
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        config.setExposedHeaders(List.of("*"));
        
        // Headers
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With"));

        
        // Headers que o Axios/Brownsers podem "enxergar" na resposta
        config.setExposedHeaders(List.of("Authorization", "Content-Type")); // Importante para o React ler o Token
        config.setAllowCredentials(false);

        // Tempo que o navegador guarda a permissão do OPTIONS (evita requisições extras)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
