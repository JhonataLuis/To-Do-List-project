package br.com.toDoList.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.toDoList.model.Streak;
import br.com.toDoList.model.User;
import br.com.toDoList.repository.StreakRepository;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.service.IStreak;

@Service
public class StreakService implements IStreak{

    private static final Logger logger = LoggerFactory.getLogger(StreakService.class);

    @Autowired
    private StreakRepository streakRepo;

    @Autowired
    private TarefaRepository taskRepo;

    private static final ZoneId brasil = ZoneId.of("America/Sao_Paulo");

    @Override
    public void atualizarStreak(User user) {
        
        Streak streak = streakRepo.findByUser(user)
            .orElseGet(() -> {
                Streak novo = new Streak();
                novo.setUser(user);
                return novo;
            });

            // Configurado brasil para pegar o fuso horário correto 
            LocalDate hoje = LocalDate.now(brasil);

            // Verifica se já concluiu tarefa hoje
            boolean fezHoje = taskRepo.existsByUserAndDataConclusao(
                user.getId(),
                hoje.atStartOfDay(),
                hoje.atTime(23, 59, 59)
            );

            if(!fezHoje) return; // não conta streak

            if(streak.getLastCompletedDate() == null) {
                streak.setCurrentStreak(1);
            } else {
                LocalDate ontem = hoje.minusDays(1);

                if(streak.getLastCompletedDate().isEqual(ontem)) {
                    streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                } else if (!streak.getLastCompletedDate().isEqual(hoje)) {
                    streak.setCurrentStreak(1);
                }
            }

            // melhor streak
            if (streak.getCurrentStreak() > streak.getBestStreak()) {
                streak.setBestStreak(streak.getCurrentStreak());
            }

            streak.setLastCompletedDate(hoje);
            logger.info("STREAK SALVA: {}", streak.getCurrentStreak());
            streakRepo.save(streak);
    }

    // STREAK
    public int calcularStreak(Long userId) {
        List<Object[]> dias = taskRepo.countTasksGroupedByDay(userId);

        Set<LocalDate> diasCompletos = new HashSet<>();

        for (Object[] row : dias) {
            diasCompletos.add(((java.sql.Date) row[0]).toLocalDate());
        }

        int streak = 0;
        // Configurado brasil para pegar o fuso horário correto
        LocalDate hoje = LocalDate.now(brasil);

        while (diasCompletos.contains(hoje)) {
            streak++;
            hoje = hoje.minusDays(1);
        }

        logger.info("CALCULO RESUTADO STREAK: " + streak);
        return streak;
    }

    // 📊 STATS
    @Override
    public Map<String, Object> getStats(Long userId) {
        // Adicionado configuração ZoneId para pegar o fuso horário correto
        LocalDateTime inicioHoje = LocalDate.now(brasil).atStartOfDay();

        // Adicionado configuração ZoneId para pegar o fuso horário correto
        LocalDateTime agora = LocalDateTime.now(brasil);

        LocalDateTime inicioSemana = LocalDate.now().minusDays(6).atStartOfDay();

        long today = taskRepo.countByUserIdAndConcluidoTrueAndDataConclusaoBetween(
                userId, inicioHoje, agora
        );

        long week = taskRepo.countByUserIdAndConcluidoTrueAndDataConclusaoBetween(
                userId, inicioSemana, agora
        );

        long total = taskRepo.countByUserIdAndConcluidoTrue(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("today", today);
        map.put("week", week);
        map.put("total", total);

        logger.info("RESULTADO STATS: " + map);
        return map;
    }

    // 📅 HISTORY (últimos 7 dias) MÉTODO PARA MOSTRAR O HISTÓRICO DOS ÚLTIMOS 7 DIAS (De segunda a domingo) 
    @Override
    public List<Map<String, Object>> getHistory(Long userId) {
        List<Object[]> raw = taskRepo.countTasksGroupedByDay(userId);

        Map<LocalDate, Long> map = new HashMap<>();

        for (Object[] row : raw) {
            map.put(
                ((java.sql.Date) row[0]).toLocalDate(),
                (Long) row[1]
            );
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            // Adicionado configuração ZoneId para pegar o fuso horário correto
            LocalDate dia = LocalDate.now(brasil).minusDays(i);

            Map<String, Object> item = new HashMap<>();
            item.put("date", dia.toString());
            item.put("label", dia.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.SHORT, new Locale.Builder()
                .setLanguage("pt").setRegion("BR").build()));
            item.put("completed", map.getOrDefault(dia, 0L));

            result.add(item);
        }

        logger.info("RESULT HISTORICO ULTIMOS 7 DIAS: " + result);
        return result;
    }


}
