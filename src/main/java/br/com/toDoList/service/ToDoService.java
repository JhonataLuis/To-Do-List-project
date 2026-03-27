package br.com.toDoList.service;

import java.util.List;

import br.com.toDoList.model.Tarefas;

public interface ToDoService {

    Tarefas create(Tarefas tarefas);

    List<Tarefas> list();

    Tarefas findById(Long id);

    Tarefas update(Tarefas tarefas);

    List<Tarefas> delete(Long id);

}
