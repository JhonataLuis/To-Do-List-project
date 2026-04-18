package br.com.toDoList.controllers;

import br.com.toDoList.repository.UserRepository;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.toDoList.dto.ChangePasswordRequest;
import br.com.toDoList.model.User;
import br.com.toDoList.serviceImpl.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    @Autowired
    private UserService userService;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfile(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/profile") // Update perfil usuário
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> data){
        Long id = userService.getCurrentUser().getId();
        return ResponseEntity.ok(userService.updateProfile(id, data.get("name")));
    }

    @PostMapping("/profile/photo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadPhoto(@RequestParam("photo") MultipartFile file) throws Exception {
        
        User currentUser = userService.getCurrentUser();

        String photoUrl = userService.uploadPhoto(currentUser.getId(), file);

        return ResponseEntity.ok(photoUrl);
       
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()") // Endpoint para atualizar senha do usuário
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordRequest request){
        try {
            userService.changePassword(request);
            // Retorna um objeto Map para o Axios no frontend identificar como JSON
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Senha alterada com sucesso!"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/push-token") // Endpoint para salvar/atualizar o token de push do usuário - notification
    public ResponseEntity<?> updatePushToken(@PathVariable Long id, @RequestBody Map<String, String> body){
        String token = body.get("expoPushToken");
        return userRepository.findById(id).map(user -> {
            user.setExpoPushToken(token);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
