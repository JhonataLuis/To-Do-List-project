package br.com.toDoList.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.toDoList.model.User;

public class UserDetailsImpl implements UserDetails{

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String name;
    private String profilePhoto;

    public UserDetailsImpl (Long id, String email, String password, 
                          Collection<? extends GrantedAuthority> authorities,
                          String name, String profilePhoto){

        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.name = name;
        this.profilePhoto = profilePhoto;
    }

    public static UserDetailsImpl build(User user){
        return new UserDetailsImpl(
            user.getId(), 
            user.getEmail(), 
            user.getSenha(), 
            user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList()),
            user.getUsername(),
            user.getProfilePhoto()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() { return true; }
    public boolean isAccountNonLocked() { return true; }
    public boolean isCredentialsNonExpired() { return true; }
    public boolean isEnabled() { return true; }

}
