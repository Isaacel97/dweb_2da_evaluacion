package com.example.evaluacion_2.prueba;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Prueba {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Encode a password
        String rawPassword = "12345678";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);

        // Verify a password
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Password Matches: " + matches);
    }
}