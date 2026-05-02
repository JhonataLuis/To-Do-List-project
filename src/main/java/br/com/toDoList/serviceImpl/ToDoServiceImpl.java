package br.com.toDoList.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.toDoList.enums.RecorrenciaTipo;
import br.com.toDoList.enums.TaskCategory;
import br.com.toDoList.enums.TaskPriority;
import br.com.toDoList.model.Tarefas;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.repository.UserRepository;

@Service
@Transactional // Garente consistência nas operações
public class ToDoServiceImpl {

    private static final Logger logger = Logger.getLogger(ToDoServiceImpl.class);

    // Imutabilidade (final nos repositories) padrão enterprise
    private final TarefaRepository taskRepo;

    private final UserRepository userRepo;

    // Forma mais segura para fazer injeção dos repository
    public ToDoServiceImpl(TarefaRepository taskRepo, UserRepository userRepo){
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    /**
     * Criar Tarefa vinculada ao Usuário
     */
    public Tarefas create(Tarefas tarefas, Long userId){
        // Logging profissional
        logger.infof("Criando tarefa parao o usuário ID: %d", userId);

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

       tarefas.setUser(user);

       return taskRepo.save(tarefas);

    }

    // Método para listar as tarefas paginadas do usuário logado com ordenação
    public Page<Tarefas> findAllPageList(Long userId, Pageable pageable){
        logger.infof("Listando tarefas do usuário ID: d%", userId);

        //ORDENANDO POR PRIORIDADES NA LISTA DE TAREFAS E ORDENAR POR TÍTULO
        Sort sort = Sort.by(
            Sort.Order.desc("prioridade"),
            Sort.Order.asc("titulo")
          );

        // Aplica a ordenação na paginação
        Pageable page = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            sort
        );

        // Chamando o repositorio filtrando pelo usuário 
        return taskRepo.findByUserId(userId, page);
    }

    // Método para listar tarefas por data (PAGINA AGENDA)
    public List<Tarefas> buscarPorData(Long userId, LocalDate data){
        logger.infof("Buscando tarefas por data: %s para usuário ID: %d", data, userId);

        LocalDateTime inicio = data.atStartOfDay(); // 00:00
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        
        return taskRepo
            .findByUserIdAndDueDateBetweenOrderByPosicaoAscDataCriacaoDesc(userId, inicio, fim)
            .stream() // Uso de Stream API
            .filter(Objects::nonNull) // proteção extra
            .toList();
    }

    // Buscar tarefa garantindo segurança / Vê somente as tarefas do usuário
    public Tarefas getTarefas(Long id, Long userId){
        logger.infof("Busca tarefa ID: %d garantindo segurança do usuário ID: %d",id, userId);
        
       return taskRepo.findById(id)
            .filter(task -> task.getUser().getId().equals(userId)) // com Stream API
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso negado"));
    }

    /**
     * Atualiza uma tarefa existente.
     * @param tarefas A tarefa com os dados atualizados/ Atualiza a tarefa referente ao usuário dela.
     * @return A tarefa atualizada.
     */
    public Tarefas update(Long id, Tarefas taskDetails, Long userId){
        logger.infof("Atualizando tarefa ID: %d para usuário ID: %d", id, userId);

        Tarefas task = getTarefas(id, userId);
        
        updateFields(task, taskDetails); // Método abaixo com os setTarefas

        return taskRepo.save(task);
    }

    // Método para deletar uma tarefa do usuário
    public void deleteTask(Long id, Long userId){
        logger.infof("Deletando tarefa ID: %d para usuário ID: %d", id, userId);

        taskRepo.delete(getTarefas(id, userId));
    }

    /**
     * Atualiação desacoplada (boa prática)
     */
    private void updateFields(Tarefas task, Tarefas details) {

        task.setTitulo(details.getTitulo());
        task.setDescricao(details.getDescricao());
        task.setStatus(details.getStatus());
        task.setConcluido(details.isConcluido());
        task.setPrioridade(TaskPriority.fromString(details.getPrioridade().toString()));
        task.setCategoria(TaskCategory.fromString(details.getCategoria().toString()));
        task.setRecorrencia(details.getRecorrencia());
        task.setDueDate(details.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());
    }

    // Método para Recorrência de tarefas
    public LocalDateTime calcularProximaData(LocalDateTime dataAtual, RecorrenciaTipo tipo) {
        logger.infof("Canculando próxima data: %d para tarefa recorrência Tipo: %d", dataAtual, tipo);
        
        LocalDateTime base = Objects.requireNonNullElse(dataAtual, LocalDateTime.now());

        return switch (tipo) { // Uso moderno de switch
            case DIARIA -> base.plusDays(1);
            case SEMANAL -> base.plusWeeks(1);
            case MENSAL -> base.plusMonths(1);
            default -> base;
        };
    }

}
