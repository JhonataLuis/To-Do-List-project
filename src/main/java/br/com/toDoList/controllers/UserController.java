package br.com.toDoList.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.serviceImpl.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfile(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/profile/{id}") // Update perfil usuário
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> data){
        Long id = userService.getCurrentUser().getId();
        return ResponseEntity.ok(userService.updateProfile(id, data.get("name"), data.get("preferneces")));
    }

    public ResponseEntity<?> uploadPhoto(@RequestParam("photo")){

    }
}
