package br.com.toDoList.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
        repository.save(tarefas);
        //return list();//RETORNA A LISTA DESENVOLVIDA ABAIXO //MODELO DRY PARA NÃO REPETIR CÓDIGO
        return tarefas;
    }

    public List<Tarefas> list(){
        //ORDENANDO POR PRIORIDADES NA LISTA DE TAREFAS E ORDENAR POR NOME
        Sort sort = Sort.by("prioridade").descending().and(
            Sort.by("titulo").ascending());
        repository.findAll();
        return repository.findAll(sort);
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

    public List<Tarefas> delete(Long id){
        repository.deleteById(id);
        return list();
    }
}
