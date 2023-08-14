package com.example.evaluacion_2.model.entities;

import com.example.evaluacion_2.model.entities.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="users")
public class Usuario {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private String id;
    @Column(length = 255)
    @Size(min = 8, max = 255)
    private String password;
    @Transient
    private String confirmPassword;
    @Transient
    @Size(min = 8, max = 255)
    private String newPassword;
    private boolean enabled = true;
    private String nombre;
    private String apellido;
    private String email;
    private String bachillerato;
    private String profesion;
    private String lugar_trabajo;
    private Integer  id_lugar_nacimiento;
    @Size(max = 100)
    public String secreto;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "puestos", referencedColumnName = "id")
    private  Rol rol;
}
