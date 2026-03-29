package br.com.toDoList.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@EnableAsync
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM = ""; // Endereço de email que aparecerá como remetente

    /**
     * 
     * @param email Envia e-mail de boas-vindas após o registro.
     * @param name
     */
    @Async
    public void sendWelcome(String email, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(email);
        message.setSubject("Bem vindo ao KeePeace Manager!");
        message.setText("Olá " + name + ", \n\nSua conta foi criada com sucesso! " +
            "Estamos felizes em ter você conosco.");

            mailSender.send(message);
    }

    /**
     * Envia o link/token de recuperação de senha.
     */
    @Async
    public void sendResetPassword(String email, String token) {
        // Exemplo de link que o usuário clicaria no front-end
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(email);
        message.setSubject("Recuperação de Senha - KeePeace Manager");
        message.setText("Você solicitou a alteração de sua senha.\n\n" +
            "Clique no link abaixo para redefinir sua senha:\n" + resetLink +
            "\n\nEste link expira em 1 hora.");

            mailSender.send(message);
    }
}
