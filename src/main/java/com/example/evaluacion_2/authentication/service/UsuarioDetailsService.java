package com.example.evaluacion_2.authentication.service;
import java.util.ArrayList;
import java.util.List;

import com.example.evaluacion_2.model.entities.Rol;
import com.example.evaluacion_2.model.entities.Usuario;
import com.example.evaluacion_2.model.repositories.RolRepository;
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
    @Autowired
    private RolRepository repoRol;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario user = repo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        int puestos = user.getRol().getId();
        Rol roles = repoRol.findById(puestos);
        authorities.add(new SimpleGrantedAuthority(roles.getAuthority()));
        System.out.println("Autoridades: " + authorities + " user = " + user );
       return new User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }

}