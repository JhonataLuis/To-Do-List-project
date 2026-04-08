package br.com.toDoList.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.model.User;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefas, Long>{

    Page<Tarefas> findByUser(User user, Pageable pageable);

    /**
     * Busca todas as tarefas associadas a um ID de usuário específico.
     * O Spring Data JPA injetará automaticamente a paginação e a ordenação
     * através do parâmetro Pageable.
     */
    Page<Tarefas> findByUserId(Long userId, Pageable pageable);

    //@Query("SELECT t FROM tarefas t WHERE t.user = :user OR :user MEMBER OF t.sharedTasks")
    //Page<Tarefas> findAccessibleTasks(@Param("user") User user, Pageable pageable);

    //long countByUserAndStatus(User user, TaskStatus status);

    // Contal total de tarefas do usuário
    long countByUserId(Long userId);

    // Conta tarefas por status de conclusão
    long countByUserIdAndConcluido(Long userId, boolean concluido);
   
}
