package br.com.toDoList.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.model.Streak;
import br.com.toDoList.model.User;
import br.com.toDoList.repository.StreakRepository;
import br.com.toDoList.service.StreakService;
import br.com.toDoList.serviceImpl.UserService;

@RestController
@RequestMapping("/api/streaks")
public class StreakController {

    private static final Logger logger = LoggerFactory.getLogger(StreakController.class);

    @Autowired
    private StreakRepository streakRepo;

    @Autowired
    private StreakService streakService;

    @Autowired
    private UserService userService;

   /*  @GetMapping("/streak")
    public ResponseEntity<Streak> getStreak(@AuthenticationPrincipal User user) {
        Streak streak = streakRepo.findByUser(user)
            .orElse(null);

            return ResponseEntity.ok(streak);
    }*/

    @GetMapping("/streak")
    public Map<String, Object> getStreak(@AuthenticationPrincipal User user) {
        Long userId = userService.getCurrentUser().getId();
        int streak = streakService.calcularStreak(userId);
        return Map.of("streak", streak);
    }

    // 📊 STATS
   @GetMapping("/stats")
    public Map<String, Object> getStats(@AuthenticationPrincipal User user) {
      Long userId = userService.getCurrentUser().getId();
    return streakService.getStats(userId);
}

    // 📅 HISTORY
    @GetMapping("/history")
    public List<Map<String, Object>> getHistory(@AuthenticationPrincipal User user) {

        logger.info("USER AUTH:" + user);
        logger.info("USER SERVICE: " + userService.getCurrentUser());
        
        Long userId = userService.getCurrentUser().getId();
        return streakService.getHistory(userId);
    }

}
