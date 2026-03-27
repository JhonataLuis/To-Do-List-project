package br.com.toDoList.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.serviceImpl.ToDoServiceImpl;



/**
 *
 * A sample greetings controller to return greeting text
 */

@RestController
public class ToDoControllers {
	
    @Autowired
    private ToDoServiceImpl service;
	
    
    @RequestMapping(value = "/mostrarnome/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String greetingText(@PathVariable String name) {
        return "Spring Boot REST API" + name + "!";
    }
    
    
    /*MÉTODO DA API PARA CONSULTAR TODOS AS TAREFAS CADASTRADAS*/
    @GetMapping(value = "/listartodos")/*ESSE É UM METODO DE API*/
    @ResponseBody/*RETORNA OS DADOS PARA O CORPO DA RESPONSTA JSON*/
    public ResponseEntity<List<Tarefas>> listarTarefas(){
    	
        List<Tarefas>  tarefas = service.list();
    	
    	return new ResponseEntity<List<Tarefas>>(tarefas, HttpStatus.OK);/*RETORNAR A LISTA EM JSON*/
    }

    /*ENDPOINT COM PAGINAÇÃO*/
    @GetMapping(value = "/tarefas/paginadas")
    @ResponseBody
    public ResponseEntity<Page<Tarefas>> listarTaskPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size ){
        
            try{
                // Cria objeto Pageable com página e tamanho
                Pageable pageable = PageRequest.of(page, size);

                /*BUSCA TAREFAS COM PAGINAÇÃO (JÁ APLICA ORDENAÇÃO) */
                Page<Tarefas> taskPage = service.findAllPagelist(pageable);

                return new ResponseEntity<>(taskPage, HttpStatus.OK);
                
            } catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
    
    /*MÉTODO DA API PARA SALVAR UMA TAREFA NO BANCO DE DADOS*/
    @PostMapping(value = "/tarefas")
    @ResponseBody
    public ResponseEntity<Tarefas> salvar(@RequestBody Tarefas tarefas){
    	
        Tarefas taref = service.create(tarefas);
    	
    	return new ResponseEntity<Tarefas>(taref, HttpStatus.CREATED);
    }
    
   
    /*MÉTODO DA API PARA ATUALIZAR TAREFA NO BANCO DE DADOS*/
    @PutMapping(value = "/tarefas/{id}")
    @ResponseBody
    public ResponseEntity<Tarefas> update(@PathVariable Long id, @RequestBody Tarefas tarefas){
    	tarefas.setId(id);
        Tarefas taref = service.update(tarefas);
    	
    	return new ResponseEntity<Tarefas>(taref, HttpStatus.OK);
    }
    

     /*MÉTODO DA API PARA DELETAR UMA TAREFA CADASTRADA DO BANCO DE DADOS*/
    @DeleteMapping(value = "/tarefas/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id){
    	
        Tarefas tasks = service.findById(id);
        if (tasks == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.delete(id);
    	
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
