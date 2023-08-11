package com.example.evaluacion_2.model.entities;

import com.example.evaluacion_2.model.entities.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="users")
public class Usuario {
    @Id
    @Column(length = 50, unique = true)
    @NotEmpty
    private String username;
    @NotEmpty
    @Column(length = 255)
    @Size(min = 8, max = 255)
    private String password;
    private boolean enabled;
    private String nombre;
    private String apellido;
    private String email;
    private String bachillerato;
    private String profesion;
    private String lugar_trabajo;
    private long id_lugar_nacimiento;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<Rol> authorities;
}
