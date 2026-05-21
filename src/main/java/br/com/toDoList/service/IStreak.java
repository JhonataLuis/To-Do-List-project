package br.com.toDoList.service;

import java.util.List;
import java.util.Map;

import br.com.toDoList.model.User;

public interface IStreak {

    void atualizarStreak(User user);

    int calcularStreak(Long userId);

    Map<String, Object> getStats(Long userId);

    List<Map<String, Object>> getHistory(Long userId);
}
