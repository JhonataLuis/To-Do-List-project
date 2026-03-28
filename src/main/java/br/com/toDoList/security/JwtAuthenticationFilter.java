package br.com.toDoList.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.toDoList.serviceImpl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
            try {
                String jwt = recoveryToken(request);
                if(jwt != null && jwtUtils.validateToken(jwt)){
                    String email = jwtUtils.getEmailFromToken(jwt);
                    UserDetails user = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                 
                }
            } catch (Exception ex){
                logger.error("Auth error: {}", ex.getMessage());
                SecurityContextHolder.clearContext(); // Limpa contexto se houver erro
            }
           filterChain.doFilter(request, response);
        }

        private String recoveryToken(HttpServletRequest request){
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }
            return authHeader.substring(7);
        }
}
