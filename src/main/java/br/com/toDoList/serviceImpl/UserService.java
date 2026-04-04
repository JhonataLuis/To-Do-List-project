package br.com.toDoList.serviceImpl;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.toDoList.dto.ChangePasswordRequest;
import br.com.toDoList.model.User;
import br.com.toDoList.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado no contexto de segurança!"));
    }

    public User updateProfile(Long id, String name){
        User user = userRepo.findById(id).get();
        if (name != null) user.setUsername(name);
        return userRepo.save(user);

    }

    // Método para cadastrar a foto do perfil do usuário
    public String uploadPhoto(Long id, MultipartFile file) throws Exception{
        logger.info("Tentando cadastrar foto do usuário...");
        Path uploadDir = Paths.get("uploads/profiles").toAbsolutePath().normalize();

        // Cria diretório se não existir
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        // Gera um nome único e limpa o nome original
        String originalFilename = file.getOriginalFilename();
        String cleanFilename = (originalFilename != null) ? originalFilename.replaceAll("[^a-zA-Z0-9.\\-]", "_") : "photo";
        String fileName = UUID.randomUUID() + "-" + cleanFilename;

        Path targetLocation = uploadDir.resolve(fileName);

        // Copia o arquivo usando Try-with-resources (fecha o stream automaticamente)
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        // Busca o usuário com tratamento de erro (evita o erro do .get() vazio)
        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

            // Define a URL e salva
            String photoUrl = "/uploads/profiles/" + fileName;
            user.setProfilePhoto(photoUrl);
            userRepo.save(user);
            logger.info("Cadastro da Foto do usuário: " + user.getEmail() + "e a foto: " + photoUrl);

            return photoUrl;
    }

    // Método para alterar senha do usuário
    public void changePassword(ChangePasswordRequest request){

        User user = getCurrentUser(); // pega o usuário logado

        // Se a senha atual e a adicionado como atual não for igual
        if(!encoder.matches(request.getCurrentPassword(), user.getSenha())){
            throw new RuntimeException("Senha atual incorreta! Favor informar a senha correta.");
        }

        // Aqui criptografa a nova senha e salva
        user.setSenha(encoder.encode(request.getNewPassword()));
        userRepo.save(user);

        logger.info("Senha alterada com sucesso para o usuário: {}", user.getEmail());
    }
}
