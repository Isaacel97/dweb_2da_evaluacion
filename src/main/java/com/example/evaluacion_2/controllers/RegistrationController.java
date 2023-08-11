package com.example.evaluacion_2.controllers;

import com.example.evaluacion_2.model.entities.Usuario;
import com.example.evaluacion_2.model.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UsuarioRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Usuario());
        return "registration"; // The name of your registration form view
    }

    @PostMapping("/registration")
    public String processRegistrationForm(@ModelAttribute("user") @Valid Usuario user, BindingResult result) {
        if (result.hasErrors()) {
            return "registration"; // Return to registration form if there are errors
        }

        // Encode the password and save the user to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/login"; // Redirect to login page after successful registration
    }

}