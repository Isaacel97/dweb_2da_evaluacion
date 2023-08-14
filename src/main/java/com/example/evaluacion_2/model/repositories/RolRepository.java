package com.example.evaluacion_2.model.repositories;

import com.example.evaluacion_2.model.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, String> {
    public Rol findById(int id);


}
