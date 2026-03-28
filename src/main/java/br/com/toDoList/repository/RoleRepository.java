package br.com.toDoList.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toDoList.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByEmail(String name);
}
