package com.example.evaluacion_2.controllers;

import com.example.evaluacion_2.model.entities.Rol;
import com.example.evaluacion_2.model.entities.Usuario;
import com.example.evaluacion_2.model.repositories.RolRepository;
import com.example.evaluacion_2.model.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Autowired
    private RolRepository reporol;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Usuario());
        return "registration"; // The name of your registration form view
    }

    @PostMapping("/registration")
    public String processRegistrationForm(@ModelAttribute("user") @Valid Usuario user, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("Error(s) en el formulario de registro: " + result.getAllErrors());
            return "registration"; // Return to registration form if there are errors
        }
        if(result.getFieldValue("secreto").equals("")) {
            //add custom validation error to the result object
            result.rejectValue("secreto", "secreto.invalid", "El secreto no es válido");
            return "registration"; // Return to registration form if there are errors
        }
//        Confirmar contraseña
        if (!user.getPassword().equals(result.getFieldValue("confirmPassword"))){
            System.out.println("Error en la confirmación de la contraseña: " + user.getPassword() + " != " + result.getFieldValue("confirmPassword"));
            result.rejectValue("confirmPassword", "password.mismatch", "Las contraseñas no coinciden");
            return "registration"; // Return to registration form if there are errors
        }
        String allowedDomain = "uteq.edu.mx";

        if (!user.getEmail().endsWith("@" + allowedDomain)){
            System.out.println("Error en el dominio del correo electrónico: " + user.getEmail());
            result.rejectValue("email", "email.invalid", "Registro unicamente para correos institucionales " + allowedDomain);
            return "registration"; // Return to registration form if there are errors
        }

        // Encode the password and save the user to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Rol por defecto: ROLE_USER
        Rol rol = reporol.findById(2);
        user.setRol(rol);

        userRepository.save(user);
        System.out.println("Usuario registrado exitosamente: " + user);

        return "redirect:/login"; // Redirect to login page after successful registration
    }

    @GetMapping("/recuperar_contrasena")
    public String showRecuperarContrasenaForm(Model model) {
        model.addAttribute("user", new Usuario());
        model.addAttribute("secreto_exitoso",  "false");

        return "recuperar_contrasena"; // The name of your registration form view
    }

    @PostMapping("/recuperar_contrasena")
    public String recuperarContrasena(HttpServletRequest request, Model model, @ModelAttribute("user") @Valid Usuario user, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Error(s) en el formulario de recuperar_contrasena: " + result.getAllErrors());
            return "recuperar_contrasena"; // Return to registration form if there are errors
        }

        Usuario usuarioFromDatabase = userRepository.findByEmail(user.getEmail());
        if(usuarioFromDatabase == null) {
            //add error message for the email field
            result.rejectValue("email", "email.invalid", "El correo electrónico ingresado no está registrado.");
            return "recuperar_contrasena";
        }
        String secretoFromDatabase = usuarioFromDatabase.getSecreto();

        if (secretoFromDatabase == null || !secretoFromDatabase.equals(user.getSecreto())) {
            //add error message for the secreto field
            result.rejectValue("secreto", "secreto.invalid", "El secreto ingresado no es correcto.");
            return "recuperar_contrasena";
        }

        System.out.println("Recuperando contraseña para usuario: " + usuarioFromDatabase);
        HttpSession session = request.getSession();
        session.setAttribute("recuperacionExitosa", true);
        session.setAttribute("user", usuarioFromDatabase);
        return "redirect:/recuperar_contrasena/actualizar";
    }

    @GetMapping("/recuperar_contrasena/actualizar")
    public String showActualizarContrasenaForm(HttpServletRequest request, Model model) {
        //user must come from recuperarContrasena method
        HttpSession session = request.getSession();
        model.addAttribute("user", new Usuario());
        Boolean recuperacionExitosa = (Boolean) session.getAttribute("recuperacionExitosa");
        if (recuperacionExitosa == null || !recuperacionExitosa) {
            return "redirect:/recuperar_contrasena";
        }
        return "actualizar_contrasena";
    }

    @PostMapping("/recuperar_contrasena/actualizar")
    public String actualizar_contrasena(HttpServletRequest request, Model model,@ModelAttribute("user") @Valid Usuario user, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Error(s) en el formulario de actualizar_contrasena: " + result.getAllErrors());
            return "actualizar_contrasena"; // Return to registration form if there are errors
        }
        HttpSession session = request.getSession();
        Usuario usuarioFromDatabase = (Usuario) session.getAttribute("user");
        System.out.println("Actualizando contraseña para usuario: " + usuarioFromDatabase);
        if(usuarioFromDatabase == null) {
            System.out.println("Error en la sesión de recuperación de contraseña: " + usuarioFromDatabase);
            return "redirect:/recuperar_contrasena";
        }
        System.out.println("Confirmar contrasena" +  result.getFieldValue("confirmPassword"));

        String oldPassword = usuarioFromDatabase.getPassword();
        String newPassword = result.getFieldValue("newPassword").toString();
        String confirmPassword = result.getFieldValue("confirmPassword").toString();

        if (!confirmPassword.equals(newPassword)){
            System.out.println("Error en la confirmación de la contraseña: " + newPassword + " != " + confirmPassword);
            result.rejectValue("confirmPassword", "password.mismatch", "Las contraseñas no coinciden");
            return "actualizar_contrasena" ;
        }

        System.out.println("Actualizando contraseña para usuario: " + usuarioFromDatabase);
//        // Encode the password and save the user to the database
        usuarioFromDatabase.setPassword(passwordEncoder.encode(newPassword.toString()));
        try {
            userRepository.save(usuarioFromDatabase);
        } catch (Exception e) {
            System.out.println("Error al actualizar la contraseña: " + e.getMessage());
            return "actualizar_contrasena" ;
        }
        userRepository.save(usuarioFromDatabase);
        System.out.println("Contraseña actualizada exitosamente: " + user);
        model.addAttribute("actualizacion_exitosa",  "true");
        return "actualizar_contrasena" ;
    }
}