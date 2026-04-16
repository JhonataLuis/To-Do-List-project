package br.com.toDoList.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.dto.PasswordResetConfirm;
import br.com.toDoList.dto.PasswordResetRequest;
import br.com.toDoList.serviceImpl.AuthService;
import jakarta.validation.Valid;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class PasswordController {


    @Autowired
    private AuthService authService;

    @PostMapping("/forgot-password") // Endpoint para enviar email para recuperar senha
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequest request){
        System.out.println("Recebi uma requisição para o email: " + request.getEmail());
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso.");
    }

    @PostMapping("/reset-password") // Endp
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetConfirm request){

        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Senha alterada com sucesso");
    }

}
