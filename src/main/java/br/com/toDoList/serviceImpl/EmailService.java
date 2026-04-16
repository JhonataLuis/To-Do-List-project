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

    private final String FROM = "jhonatafonte23@gmail.com"; // Endereço de email que aparecerá como remetente

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
        message.setText("Olá " + name + ", \n\nSua conta foi criada com sucesso!");
        mailSender.send(message);
    }

    /**
     * Envia o token de 6 dígitos para recuperação de senha no App.
     */
    @Async
    public void sendResetPassword(String email, String token) {
        // Exemplo de link que o usuário clicaria no front-end
        //String resetLink = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(email);
        message.setSubject("Recuperação de Senha - KeePeace Manager");

        // Texto focado no uso dentro do aplicativo
        message.setText(
            "Você solicitou a redefinição de sua senha.\n\n" +
            "Use o código abaixo dentro do aplicativo para cadastrar um nova senha:\n" + 
            "CÓDIGO: " + token + "\n\n" +
            "Este código é valido por 1 hora.\n"
        );

            mailSender.send(message);
    }
}
