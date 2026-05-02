package br.com.toDoList.serviceImpl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.toDoList.dto.AuthResponse;
import br.com.toDoList.dto.LoginRequest;
import br.com.toDoList.dto.RegisterRequest;
import br.com.toDoList.model.User;
import br.com.toDoList.repository.RoleRepository;
import br.com.toDoList.repository.UserRepository;
import br.com.toDoList.security.JwtUtils;
import br.com.toDoList.security.UserDetailsImpl;

@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AuthenticationManager authManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final PasswordEncoder encoder;

    private final EmailService emailService;

    // Injeção por construtor (padrão recomendado)
    public AuthService(AuthenticationManager authManager, 
                       JwtUtils jwtUtils,
                       UserRepository userRepo, 
                       RoleRepository roleRepo,
                       PasswordEncoder encoder, 
                       EmailService emailService) {

        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    /**
     *  Método para Realizar Login
     */
    public AuthResponse login(LoginRequest request) {
        logger.debug("Tentando autenticação para email: {}", request.getEmail());

        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword()
            )
        );

            UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
            String token = jwtUtils.generateToken(auth);

            Set<String> roles = user.getAuthorities()
            .stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toSet());

            logger.info("Usuário autenticado com sucesso: {}", user.getUsername());

            return new AuthResponse(
                token, "Bearer", 
                user.getId(), 
                user.getName(), 
                user.getUsername(), 
                roles, 
                user.getProfilePhoto()
            );
    }

    /**
     * Método para Registrar um novo usuário no sistema Tasks
     */
    // Método para Registrar um novo usuário
    public void register(RegisterRequest request){
        logger.info("Registrando novo usuário: {}",request.getEmail());

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        var roleUser = roleRepo.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Role USER não encontrado"));

        User user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setSenha(encoder.encode(request.getPassword()));
        user.setRoles(Set.of(roleUser));

        // Salva usuário no banco primeiro
        userRepo.save(user);

        logger.info("Usuário salvo com sucesso: {}", user.getEmail());

        // Envia o e-mail de boas vindas
        emailService.sendWelcome(user.getEmail(), user.getUsername());
        logger.info("Usuário registrado e e-mail de boas-vindas enviado para fila.");
    }

    /**
     * 
     * ESQUECI MINHA SENHA
     */
    // Método para recuperar a senha do usuário
    public void forgotPassword(String email){

        logger.info("Solicitação de recuperação de senha para: {}", email);

        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Gera um código de 6 digitos aleátórios
            String token = generateResetToken();

            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

            userRepo.save(user);

            emailService.sendResetPassword(email, token);

            logger.info("Token de recuperação enviado para: {}", email);
    }

    /**
     *  RESET DE SENHA
     */
    // Método para definir a nova senha usuando o token
    public void resetPassword(String token, String newPassword){
        
        String cleanToken = token.trim();

        // Busca o usuário que possui aquele código específico
        User user = userRepo.findByResetToken(cleanToken)
            .orElseThrow(() -> new RuntimeException("Token inválido"));

            // Verifica se o tempo de expiração já passou
            if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {

                throw new RuntimeException("Token expirado");
            }

            validaPassword(newPassword); // Método abaixo

            // Criptografa a nova senha antes de salvar no banco
            // IMPORTANTE: Nunca salve a senha em texto puro!
            user.setSenha(encoder.encode(newPassword));
            // Limpa o token e a expiração (Segurança: o código só funciona uma vez)
            user.setResetToken(null);// Limpa o token para não ser usado novamente
            user.setResetTokenExpiry(null);

            userRepo.save(user);

            logger.info("Senha redefinida com sucesso para: {}", user.getEmail());
    }

    /**
     *  MÉTODOS PRIVADOS
     */
    // Método para gerar um token aleatório
    private String generateResetToken() {
        int number = RANDOM.nextInt(900000) + 100000;
        return String.valueOf(number);
    }

    // Método para validar a senha
    private void validaPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new RuntimeException("Senha deve ter no mínimo 6 caracteres");
        }
    }

}
