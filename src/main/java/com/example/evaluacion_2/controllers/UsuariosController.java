package com.example.evaluacion_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuariosController {

    @GetMapping("/home")
    public String usuarios(){
        return "usuarios/index";
    }

}
