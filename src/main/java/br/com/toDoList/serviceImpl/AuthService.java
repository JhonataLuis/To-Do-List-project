package br.com.toDoList.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.toDoList.dto.AuthResponse;
import br.com.toDoList.dto.LoginRequest;
import br.com.toDoList.dto.RegisterRequest;
import br.com.toDoList.model.User;
import br.com.toDoList.repository.RoleRepository;
import br.com.toDoList.repository.UserRepository;
import br.com.toDoList.security.JwtUtils;
import br.com.toDoList.security.UserDetailsImpl;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    // Método para Realizar Login
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
            String token = jwtUtils.generateToken(auth);

            Set<String> roles = new HashSet<>();
            user.getAuthorities().forEach(a -> roles.add(a.getAuthority()));

            return new AuthResponse(token, "Bearer", user.getId(), user.getName(), 
                               user.getUsername(), roles, user.getProfilePhoto());
    }

    // Método para Registrar um novo usuário
    public void register(RegisterRequest request){
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setSenha(encoder.encode(request.getPassword()));
        user.setRoles(Set.of(roleRepo.findByName("USER")
        .orElseThrow(() -> new RuntimeException("Role USER not found"))));
        userRepo.save(user);
        System.out.println("ROLE SALVO NO BANCO");

        //emailService.sendWelcome(user.getEmail(), user.getUsername());
    }

    // Método para recuperar a senha do usuário
    public void forgotPassword(String email){
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Gera um código de 6 digitos aleátórios
            String token = String.valueOf(new Random().nextInt(899999) + 100000);

            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepo.save(user);

            emailService.sendResetPassword(email, token);
    }

    // Método para definir a nova senha usuando o token
    public void resetPassword(String token, String newPassword){
        // Busca o usuário que possui aquele código específico
        User user = userRepo.findByResetToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido ou não encontrado."));

            // Verifica se o tempo de expiração já passou
            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Este código expirou. Solicite um novo e-mail.");
            }

            // Criptografa a nova senha antes de salvar no banco
            // IMPORTANTE: Nunca salve a senha em texto puro!
            user.setSenha(encoder.encode(newPassword));
            // Limpa o token e a expiração (Segurança: o código só funciona uma vez)
            user.setResetToken(null);// Limpa o token para não ser usado novamente
            user.setResetTokenExpiry(null);

            userRepo.save(user);
    }


}
