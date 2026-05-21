package br.com.toDoList.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.toDoList.enums.RecorrenciaTipo;
import br.com.toDoList.model.Tarefas;

public interface IToDoService {

    Tarefas create(Tarefas tarefas, Long userId);

    Page<Tarefas> findAllPageList(Long userId, Pageable pageable);

    List<Tarefas> buscarPorData(Long userId, LocalDate data);

    Tarefas update(Long id, Tarefas taskDetails, Long userId);

    void deleteTask(Long id, Long userId);

    Tarefas getTarefas(Long id, Long userId);

    LocalDateTime calcularProximaData(LocalDateTime dataAtual, RecorrenciaTipo tipo);

    

    

    

}
