package br.com.toDoList.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.toDoList.repository.UserRepository;
import br.com.toDoList.security.UserDetailsImpl;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // Isso mantém a conexão aberta para ler as Roles
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return UserDetailsImpl.build(userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    
}
 