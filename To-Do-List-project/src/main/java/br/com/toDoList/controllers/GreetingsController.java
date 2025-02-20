package br.com.toDoList.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.toDoList.model.Usuario;
import br.com.toDoList.repository.UserRepository;




/**
 *
 * A sample greetings controller to return greeting text
 */

@RestController
public class GreetingsController {
	
	@Autowired
    private UserRepository userRepository;
	
    
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
    
   
}
