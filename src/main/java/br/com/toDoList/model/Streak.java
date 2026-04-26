package br.com.toDoList.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "streaks")
@SequenceGenerator(name = "seq_streak", sequenceName = "seq_streak", allocationSize = 1, initialValue = 1)
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_streak")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private int currentStreak;

    private int bestStreak;

    private LocalDate lastCompletedDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setLastCompletedDate(LocalDate lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }

    public LocalDate getLastCompletedDate() {
        return lastCompletedDate;
    }

}
