package br.com.toDoList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.toDoList.model.Tarefas;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefas, Long>{

    
}
