package br.com.toDoList.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.model.Tarefas;
import br.com.toDoList.model.Usuario;
import br.com.toDoList.repository.TarefaRepository;
import br.com.toDoList.repository.UserRepository;




/**
 *
 * A sample greetings controller to return greeting text
 */

@RestController
public class GreetingsController {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private TarefaRepository tafefaRepository;
	
    
    @RequestMapping(value = "/consultarnome/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String greetingText(@PathVariable String name) {
        return "Hello World" + name + "!";
    }
    
    @RequestMapping(value = "/olamundo/{nome}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String retornaOlaMundo(@PathVariable String nome) {
    	
    	Usuario usuario = new Usuario();
    	usuario.setNome(nome);
    	
    	userRepository.save(usuario);
    	
    	return "Hello World - Welcome to Portugal : " +nome+" ! ";
    	
    }
    
    /*CRIANDO CRONTROLADOR PARA SALVAR TAREFA NO BANCO DE DADOS POSTGRESQL*/
    @PostMapping(value = "cadastrarTarefas")
    @ResponseBody
    public ResponseEntity<Tarefas> salvar(@RequestBody Tarefas tarefas){
    	
    	Tarefas taref = tafefaRepository.save(tarefas);
    	
    	return new ResponseEntity<Tarefas>(taref, HttpStatus.CREATED);
    	
    }
    
   
}
