package br.com.toDoList.serviceImpl;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.toDoList.model.User;
import br.com.toDoList.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).get();
    }

    public User updateProfile(Long id, String name, String preferences){
        User user = userRepo.findById(id).get();
        if (name != null) user.setUsername(name);
        if (preferences != null) user.setPreferences(preferences);
        return userRepo.save(user);

    }

    public String uploadPhoto(Long id, MultipartFile file) throws Exception{
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

            return photoUrl;
    }
}
