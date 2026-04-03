package br.com.toDoList.controllers;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.enums.TaskStatus;
import br.com.toDoList.model.Tarefas;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.serviceImpl.ToDoServiceImpl;
import br.com.toDoList.serviceImpl.UserService;



/**
 *
 * A sample greetings controller to return greeting text
 */

@RestController
@RequestMapping("/api/tasks")
public class ToDoControllers {

    private static final Logger logger = LoggerFactory.getLogger(ToDoControllers.class);
	
    @Autowired
    private ToDoServiceImpl taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TarefaRepository taskRepo;
	

    /*ENDPOINT COM PAGINAÇÃO*/
    @GetMapping(value = "/tarefas/paginadas")
    @ResponseBody
    public ResponseEntity<Page<Tarefas>> listarTaskPaginated(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size ){
        
            try{
                // Cria objeto Pageable com página e tamanho
                Pageable pageable = PageRequest.of(page, size);

                Long userId = userService.getCurrentUser().getId();

                /*BUSCA TAREFAS COM PAGINAÇÃO (JÁ APLICA ORDENAÇÃO) */
                Page<Tarefas> taskPage = taskService.findAllPagelist(userId, pageable);

                return new ResponseEntity<>(taskPage, HttpStatus.OK);
                
            } catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTarefas(@PathVariable Long id){
        Long userId = userService.getCurrentUser().getId();

        return ResponseEntity.ok(taskService.getTarefas(id, userId));
    }
    
    /*MÉTODO DA API PARA SALVAR UMA TAREFA NO BANCO DE DADOS*/
    @PostMapping(value = "/tarefas")
    @ResponseBody
    public ResponseEntity<Tarefas> salvar(@RequestBody Tarefas task){
    	Long userId = userService.getCurrentUser().getId();
        Tarefas tarefas = taskService.create(task, userId);
    	
    	return new ResponseEntity<Tarefas>(tarefas, HttpStatus.CREATED);
    }
    
   
    /*MÉTODO DA API PARA ATUALIZAR TAREFA NO BANCO DE DADOS*/
    @PutMapping(value = "/tarefas/{id}")
    @ResponseBody
    public ResponseEntity<Tarefas> update(@PathVariable(name = "id") Long id, @RequestBody Tarefas task){
        Long userId = userService.getCurrentUser().getId();
    	task.setId(id);
        Tarefas tarefas = taskService.update(id, task, userId);
    	
    	return new ResponseEntity<Tarefas>(tarefas, HttpStatus.OK);
    }
    

     /*MÉTODO DA API PARA DELETAR UMA TAREFA CADASTRADA DO BANCO DE DADOS*/
    @DeleteMapping(value = "/tarefas/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTask(@PathVariable(name = "id") Long id){
        // Pega o ID do usuário logado
    	Long userId = userService.getCurrentUser().getId();

        try {
            // Tenta buscar a tarefa. Se não existir ou não for do usuário,
            //o getTarefas já lança a exceção que interrompe o fluxo.
            taskService.getTarefas(id, userId);

            // Deleta a tarefa (método void, não retorna nada)
            taskService.deleteTask(id, userId);

            // Retorna sucesso sem conteúdo
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Se o getTarefas lançar "Access denied" ou "Not found"
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint do método para concluir uma tarefa
    @SuppressWarnings("null")
    @PatchMapping("/tarefas/{id}/concluir")
    public ResponseEntity<?> concluirTask(@PathVariable(name = "id") Long id){
        
        Long userId = userService.getCurrentUser().getId();

        return taskRepo.findById(id).map(task -> {

            logger.info("User logado: " +userId);
            logger.info("Dono da tarefa: " +task.getUser().getId());
            // validação de segurança: o usuário logado é dono da tarefa?
            if(!task.getUser().getId().equals(userId)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar essa tarefa");
            }

            task.setStatus(TaskStatus.DONE);
            task.setConcluido(true);
            task.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(taskRepo.save(task));
        }).orElse(ResponseEntity.notFound().build());
    }
    
}
