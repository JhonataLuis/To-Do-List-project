package br.com.toDoList.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.service.ExpoNotificationService;

@Component
@EnableScheduling
public class TaskReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TaskReminderScheduler.class);

    @Autowired
    private TarefaRepository taskRepo;

    @Autowired
    private ExpoNotificationService notifiService;

    // Roda a cada 1 minuto
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkUpcomingTasks(){

        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime window = now.plusMinutes(1);
        LocalDateTime oneMinuteAgo = now.minusMinutes(1);
        //LocalDateTime window = now.plusDays(7);

        logger.info("NOW: {}", now);
        logger.info("WINDOW: {}", oneMinuteAgo);

        // Busca tarefas não concluídas com prazo nos próximos 15 min
        List<Tarefas> tasks = taskRepo.findByDueDateBetweenAndConcluidoFalseAndNotificationSentFalse(oneMinuteAgo, now);
       
        logger.info("Tarefas encontradas para notificar: {}", tasks.size());

        // Log
        tasks.forEach(t ->
            logger.info("Task encontrada: {} - dueDate: {} - concluido: {} - notificação enviada: {}",
                t.getTitulo(),
                t.getDueDate(),
                t.isConcluido(),
                t.isNotificationSent()
            )
        );

        for (Tarefas task : tasks) {

            // Verificação de segurança
             if (task.isConcluido() || task.isNotificationSent()) {
                    logger.info("Tarefa '{}' ignorada - concluída: {}, notificação enviada: {}", 
                    task.getTitulo(), task.isConcluido(), task.isNotificationSent());
                    continue;
                }

            String token = task.getUser().getExpoPushToken();
            
            if (token != null && task.getDueDate() != null) {

                notifiService.sendNotifications(
                    token,
                    task.getTitulo(),
                    "Você tem uma tarefa agora",
                    task.getId()
                );

                // Marca como enviado para não repetir no próximo minuto
                task.setNotificationSent(true);
                logger.info("Enviando push notification task: {}", task.getId(), task.getTitulo());
            }
        }
        taskRepo.saveAll(tasks);
        logger.info("TASKS ENCONTRADAS: " + tasks.size());
    }

}
