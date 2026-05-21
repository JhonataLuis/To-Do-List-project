package br.com.toDoList.serviceImpl;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.toDoList.dto.ChangePasswordRequest;
import br.com.toDoList.model.User;
import br.com.toDoList.repository.UserRepository;
import br.com.toDoList.service.IUser;

@Service
@Transactional
public class UserService implements IUser{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String UPLOAD_DIR = "uploads/profiles";

    private final PasswordEncoder encoder;

    private final UserRepository userRepo;

    // Injeção por construtor (padrão recomendado)
    public UserService(PasswordEncoder encoder, UserRepository userRepo) {
        this.encoder = encoder;
        this.userRepo = userRepo;
    }

    /**
     *  Usuário logado (contexto de segurança)
     */
    @Override
    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

        return userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado no contexto de segurança!"));
    }

    /**
     *  Atualizar perfil do usuário logado
    */
   @Override
    public User updateProfile(Long id, String name){
        logger.info("Atualizando perfil: %d do usuário ID: %d", name, id);

        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (Objects.nonNull(name) && !name.isBlank()) { 
            user.setUsername(name.trim()); // Evita salvar null, vazio e espaços
        }

        return userRepo.save(user);

    }

    // Método para upload de foto (seguro) do perfil do usuário
    @Override
    public String uploadPhoto(Long id, MultipartFile file) {
        logger.info("Iniciando upload de foto para usuário ID: %d", id);

        validateFile(file); // Método abaixo valida arquivo

        Path uploadPath = Paths.get(UPLOAD_DIR)
            .toAbsolutePath()
            .normalize();

            createDirectoryIfNotExists(uploadPath); // Cria diretório se não existir


        // Gera um nome único e limpa o nome original
        String fileName = generateFileName(file.getOriginalFilename());

        Path targetLocation = uploadPath.resolve(fileName);

        // Copia o arquivo usando Try-with-resources (fecha o stream automaticamente)
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error("Erro ao salvar arquivo", e);
            throw new RuntimeException("Erro ao fazer upload da imagem");
        }

        // Busca o usuário com tratamento de erro (evita o erro do .get() vazio)
        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Define a URL e salva
            String photoUrl = "/uploads/profiles/" + fileName;

            user.setProfilePhoto(photoUrl);
            userRepo.save(user);

            logger.info("Upload concluído para usuário: {} | Foto: {} ", user.getEmail(), photoUrl);

            return photoUrl;
    }

    /**
     *  Método para alterar senha do usuário
     */
    @Override
    public void changePassword(ChangePasswordRequest request){

        User user = getCurrentUser(); // pega o usuário logado

        // Se a senha atual e a adicionado como atual não for igual
        if(!encoder.matches(request.getCurrentPassword(), user.getSenha())){
            throw new RuntimeException("Senha atual incorreta! Favor informar a senha correta.");
        }

        validateNewPassword(request);

        // Aqui criptografa a nova senha e salva
        user.setSenha(encoder.encode(request.getNewPassword()));
        userRepo.save(user);

        logger.info("Senha alterada com sucesso para o usuário: {}", user.getEmail());
    }

    /**
     *  MÉTODOS PRIVADOS (Clean Code)
    */
    private void validateFile(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new RuntimeException("Arquivo inválido");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Apenas imagens são permitidas");
        }
    }

    private void validateNewPassword(ChangePasswordRequest request) {
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            throw new RuntimeException("A nova senha deve ter pelo menos 6 caracteres");
        }
    }

    private void createDirectoryIfNotExists(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception e) {
            logger.error("Erro ao criar diretório de upload", e);
            throw new RuntimeException("Erro interno ao preparar upload");
        }
    }

    private String generateFileName(String originalFilename) {

        String cleanName = (originalFilename != null)
            ? originalFilename.replaceAll("[^a-zA-Z0-9.\\-]", "_")
            : "file";

            return UUID.randomUUID() + "-" + cleanName;
    }
}
