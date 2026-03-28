package br.com.toDoList.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.model.User;
import jakarta.data.repository.Param;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefas, Long>{

    Page<Tarefas> findByUser(User user, Pageable pageable);

    @Query("SELECT t FROM tarefas t WHERE t.user = :user OR :user MEMBER OF t.sharedTasks")
    Page<Tarefas> findAccessibleTasks(@Param("user") User user, Pageable pageable);

    //long countByUserAndStatus(User user, TaskStatus status);
   
}
