package br.com.toDoList.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.toDoList.model.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long>{

    
	
}
