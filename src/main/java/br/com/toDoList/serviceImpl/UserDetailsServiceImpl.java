package br.com.toDoList.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.toDoList.repository.UserRepository;
import br.com.toDoList.security.UserDetailsImpl;


@Service
@Transactional(readOnly = true) // Isso mantém a conexão aberta para ler as Roles
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepo;

    // Injeção por construtor (padrão recomendado)
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /*
    * Carrega usuário para autenticação (Spring Security)
    * @Cacheable armazena o resultado em cache para evitar múltiplas consultas ao banco
    */
    @Override
    @Cacheable("users")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Tentando autenticar usuário com email: {}", email);

        return userRepo.findByEmail(email)
            .map(UserDetailsImpl::build)
            .orElseThrow(() -> {
                logger.warn("Usuário não encontrado para email: {}", email);
                return new UsernameNotFoundException("Credenciais inválidas");
            });
    }
    
}
 