package br.com.toDoList.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExpoNotificationService {
    private final WebClient webClient;

    public ExpoNotificationService(){
        this.webClient = WebClient.builder()
            .baseUrl("https://exp.host/--/api/v2/push/send")
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public void sendNotifications(String token, String title, String body, Long taskId) {
        
        Map<String, Object> payload = Map.of(
            "to", token,
            "title", title,
            "body", body,
            "sound", "default",
            "data", Map.of(
                "taskId", taskId
            )
        );

        webClient.post()
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(response -> System.out.println("Notificação enviada: " + response),
            error -> System.out.println("Erro ao enviar notificação: " + error.getMessage()));
    }
}
