package br.com.toDoList.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.toDoList.model.Tarefas;

public interface ToDoService {

    Tarefas create(Tarefas tarefas);

    Page<Tarefas> findAllPagelist(Pageable pageable);

    Tarefas findById(Long id);

    Tarefas update(Tarefas tarefas);

    void delete(Long id);

}
