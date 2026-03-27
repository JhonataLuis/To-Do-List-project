package br.com.toDoList.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.service.ToDoService;

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private TarefaRepository repository;

    public Tarefas create(Tarefas tarefas){
        Tarefas taskSaved = repository.save(tarefas);

        if(taskSaved == null){
            throw new RuntimeException("Erro ao salvar tarefa");
        }
       
        return taskSaved;
    }

    public List<Tarefas> list() {
        // ORDENANDO POR PRIORIDADES NA LISTA DE TAREFAS E ORDENAR POR NOME
        Sort sort = Sort.by("prioridade").descending().and(
            Sort.by("titulo").ascending());
        return repository.findAll(sort);
    }

    public Page<Tarefas> findAllPagelist(Pageable pageable){
        //ORDENANDO POR PRIORIDADES NA LISTA DE TAREFAS E ORDENAR POR NOME
        Sort sort = Sort.by("prioridade").descending().and(
            Sort.by("titulo").ascending());
        //repository.findAll();

        // Aplica a ordenação na paginação
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return repository.findAll(page);
    }

    public Tarefas findById(Long id){
        return repository.findById(id).orElse(null);
    }

    /**
     * Atualiza uma tarefa existente.
     * @param tarefas A tarefa com os dados atualizados.
     * @return A tarefa atualizada.
     */

    public Tarefas update(Tarefas tarefas){
        repository.save(tarefas);

        return tarefas;
    }

    public void delete(Long id){
        repository.deleteById(id);
    }


}
