package br.com.toDoList.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.toDoList.model.Usuario;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Usuario, Long>{

	
}
