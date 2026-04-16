package br.com.toDoList.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@EnableAsync
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

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
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM); // Remetente definido
            message.setTo(email);
            message.setSubject("Bem vindo ao KeePeace!");
            message.setText("Olá " + name + ", \n\n" +
            "Sua conta foi criada com sucesso!\n" +
            "Estamos felizes em ter você conosco, para ajudar na sua organização pessoal.\n\n" +
            "Atenciosamente, \nEquipe KeePace.");
            mailSender.send(message);
            logger.info("E-mail de boas-vindas enviado com sucesso para: " + email);

        } catch (Exception e) {
            // Como é Async, o erro não trava o registro, então logamos o erro
            logger.info("Falha ao enviar e-mail de boas-vindas: " + e.getMessage());
        }
    }

    /**
     * Envia o token de 6 dígitos para recuperação de senha no App.
     */
    @Async
    public void sendResetPassword(String email, String token) {
        // Exemplo de link que o usuário clicaria no front-end

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM); // Remetente definido
        message.setTo(email);
        message.setSubject("Recuperação de Senha - KeePeace");

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
