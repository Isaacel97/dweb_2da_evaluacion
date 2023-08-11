package com.example.evaluacion_2.authentication.service;
import java.util.ArrayList;
import java.util.List;

import com.example.evaluacion_2.model.entities.Rol;
import com.example.evaluacion_2.model.entities.Usuario;
import com.example.evaluacion_2.model.repositories.UsuarioRepository;
import com.example.evaluacion_2.model.entities.Rol;
import com.example.evaluacion_2.model.entities.Usuario;
import com.example.evaluacion_2.model.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Rol rol : user.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(rol.getAuthority()));
        }
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }

}