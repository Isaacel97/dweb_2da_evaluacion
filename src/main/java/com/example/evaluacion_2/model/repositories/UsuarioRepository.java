package com.example.evaluacion_2.model.repositories;

import com.example.evaluacion_2.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    public Usuario findByEmail(String email);
    @Query("SELECT u.secreto FROM Usuario u WHERE u.id = :userId")
    String findSecretById(@Param("userId") String userId);

}
