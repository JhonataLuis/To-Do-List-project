package br.com.toDoList.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toDoList.model.Streak;
import br.com.toDoList.model.User;
import jakarta.data.repository.Repository;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long>{

    Optional<Streak> findByUser(User user);
}
