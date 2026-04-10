package br.com.toDoList.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.dto.LoginRequest;
import br.com.toDoList.dto.RegisterRequest;
import br.com.toDoList.serviceImpl.AuthService;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try{
             authService.register(request);
             // Retornar um JSON fixo ajuda o Axios no Mobile a entender o sucess
             return ResponseEntity.ok(Collections.singletonMap("message", "Usuário registraco com sucesso!"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
