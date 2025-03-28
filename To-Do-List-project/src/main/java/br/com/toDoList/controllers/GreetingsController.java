package br.com.toDoList.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.toDoList.repository.TarefaRepository;



/**
 *
 * A sample greetings controller to return greeting text
 */

@RestController
public class GreetingsController {
	
	@Autowired/*INJEÇÃO DE DEPENDENCIA IC/CI e CDI*/
	private TarefaRepository tarefaRepository;
	
    
    @RequestMapping(value = "/mostrarnome/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String greetingText(@PathVariable String name) {
        return "Spring Boot REST API" + name + "!";
    }
    
   
    @RequestMapping(value = "olamundo/{descricao}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String retornaOlaMundo(@PathVariable String descricao) {
    	
    	Tarefas tarefas = new Tarefas();
    	
    	tarefaRepository.save(tarefas);
    	
    	return "Ola mundo" + descricao;
    	
    }
    
    /*MÉTODO DA API PARA CONSULTAR TODOS AS TAREFAS CADASTRADAS*/
    @GetMapping(value = "listatodos")/*ESSE É UM METODO DE API*/
    @ResponseBody/*RETORNA OS DADOS PARA O CORPO DA RESPONSTA JSON*/
    public ResponseEntity<List<Tarefas>> listarTarefas(){
    	
    	List<Tarefas> tarefas = tarefaRepository.findAll();
    	
    	return new ResponseEntity<List<Tarefas>>(tarefas, HttpStatus.OK);/*RETORNAR A LISTA EM JSON*/
    }
    
    /*MÉTODO DA API PARA SALVAR UMA TAREFA NO BANCO DE DADOS*/
    @PostMapping(value = "salvar")
    @ResponseBody
    public ResponseEntity<Tarefas> salvar(@RequestBody Tarefas tarefas){
    	
    	Tarefas taref = tarefaRepository.save(tarefas);
    	
    	return new ResponseEntity<Tarefas>(taref, HttpStatus.CREATED);
    }
    
    /*MÉTODO DA API PARA DELETAR UMA TAREFA CADASTRADA DO BANCO DE DADOS*/
    @DeleteMapping(value = "delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestParam Long idTarefa){
    	
    	tarefaRepository.deleteById(idTarefa);
    	
    	return new ResponseEntity<String>("Tarefa deletada com sucesso", HttpStatus.OK);
    }
   
    
    /*MÉTODO DA API PARA ATUALIZAR TAREFA NO BANCO DE DADOS*/
    @PutMapping(value = "update")
    @ResponseBody
    public ResponseEntity<Tarefas> atualizar(@RequestBody Tarefas tarefa){
    	
    	Tarefas taref = tarefaRepository.saveAndFlush(tarefa);
    	
    	return new ResponseEntity<Tarefas>(taref, HttpStatus.CREATED);
    }
    
    /*MÉTODO PARA CONSULTAR UMA TAREFA DO BANCO DE DADOS PELO ID DA TAREFA*/
    @GetMapping(value = "buscartarefaid")
    @ResponseBody
    public ResponseEntity<Tarefas> consultar(@RequestParam(name = "idTarefa") Long idTarefa){
    	
    	Tarefas taref = tarefaRepository.findById(idTarefa).get();
    	
    	return new ResponseEntity<Tarefas>(taref, HttpStatus.OK);
    	
    }
    
}
