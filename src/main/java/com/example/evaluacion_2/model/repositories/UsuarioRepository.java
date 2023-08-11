package com.example.evaluacion_2.model.repositories;

import com.example.evaluacion_2.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    public Usuario findByUsername(String roleName);
}
