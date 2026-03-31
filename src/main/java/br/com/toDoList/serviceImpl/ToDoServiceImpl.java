package br.com.toDoList.serviceImpl;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.repository.UserRepository;
import br.com.toDoList.service.ToDoService;

@Service
public class ToDoServiceImpl {

    private static final Logger logger = Logger.getLogger(ToDoServiceImpl.class);

    @Autowired
    private TarefaRepository repository;

    @Autowired
    private UserRepository userRepo;

    /**
     * Método para criar uma nova tarefa para o usuário
     */
    public Tarefas create(Tarefas tarefas, Long userId){
        tarefas.setUser(userRepo.findById(userId).get());
        Tarefas taskSaved = repository.save(tarefas);

        if(taskSaved == null){
            throw new RuntimeException("Erro ao salvar tarefa");
        }
       
        return taskSaved;
    }

    public Page<Tarefas> findAllPagelist(Long userId, Pageable pageable){
        logger.info("Tentando listar Tarefas do usuario...");
        //ORDENANDO POR PRIORIDADES NA LISTA DE TAREFAS E ORDENAR POR NOME
        Sort sort = Sort.by("prioridade").descending().and(
            Sort.by("titulo").ascending());

        // Aplica a ordenação na paginação
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Chamando o repositorio filtrando pelo usuário 
        return repository.findByUserId(userId, page);
    }

    // Vê somente as tarefas do usuário
    public Tarefas getTarefas(Long id, Long userId){
        Tarefas task = repository.findById(id).orElseThrow();
        if (!task.getUser().getId().equals(userId)){
            throw new RuntimeException("Access denied");
        }

        return task;
    }

    /**
     * Atualiza uma tarefa existente.
     * @param tarefas A tarefa com os dados atualizados.
     * @return A tarefa atualizada.
     */

    public Tarefas update(Long id, Tarefas taskDetails, Long userId){
        logger.info("Tentando atualizar tarefa " + id + " para o usuário " + userId);
        Tarefas task = getTarefas(id, userId);
        task.setTitulo(taskDetails.getTitulo());
        task.setDescricao(taskDetails.getDescricao());
        task.setPrioridade(taskDetails.getPrioridade());
        task.setDueDate(taskDetails.getDueDate());
        
        return repository.save(task);
    }

    // Método para deletar uma tarefa do usuário
    public void deleteTask(Long id, Long userId){
        repository.delete(getTarefas(id, userId));
    }

    public void sharedTask(){

    }

}
