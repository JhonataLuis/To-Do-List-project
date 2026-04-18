package br.com.toDoList.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.service.ExpoNotificationService;

@Component
@EnableScheduling
public class TaskReminderScheduler {

    @Autowired
    private TarefaRepository taskRepo;

    @Autowired
    private ExpoNotificationService notifiService;

    // Roda a cada 1 minuto
    @Scheduled(cron = "0 * * * * *")
    public void checkUpcomingTasks(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime window = now.plusMinutes(15);

        // Busca tarefas não concluídas com prazo nos próximos 15 min
        List<Tarefas> tasks = taskRepo.findByDeadlineBetweenAndCompletedFalseAndNotificationSentFalse(now, window);

        for (Tarefas task : tasks) {
            String token = task.getUser().getExpoPushToken();
            if (token != null) {
                notifiService.sendNotifications(
                    token,
                    "Lembrete KeeePace",
                    "A tarefa '" + task.getTitulo() + "' vence em breve!"
                );
            }
        }
    }

}
