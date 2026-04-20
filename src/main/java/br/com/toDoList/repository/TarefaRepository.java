package br.com.toDoList.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    Page<Tarefas> findByUserIdAndConcluido(Long userId, Pageable pageable, boolean concluido);
    
    // Contal total de tarefas do usuário
    long countByUserId(Long userId);

    // Conta tarefas por status de conclusão
    long countByUserIdAndConcluido(Long userId, boolean concluido);

    // ######## Para reordenar tarefa ##########

    // Busca as tarefas ordenadas pela posição
    List<Tarefas> findByConcluidoFalseOrderByPosicaoAsc();

    @Transactional
    @Modifying
    @Query("UPDATE Tarefas t SET t.posicao = :posicao WHERE t.id = :id AND t.user.id = :userId")
    void updatePosicao(@Param("id") Long id, @Param("posicao") Integer posicao, @Param("userId") Long userId);

   // ############

   // Busca tarefas entre agora e o tempo de alerta, que não estão concluídas e não foram notificadas
   @EntityGraph(attributePaths = {"user"}) // Traz o usuário junto
   List<Tarefas> findByDueDateBetweenAndConcluidoFalseAndNotificationSentFalse(
    LocalDateTime start, LocalDateTime end
   );
}
