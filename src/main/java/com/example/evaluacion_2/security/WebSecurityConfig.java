package com.example.evaluacion_2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity //me va permitir manejar la seguridad web del proyecto
@Configuration //para decir que la clase es de configuracion springframework
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean //para que se pueda utilizar en otras clases y se pueda inyectar
    public static BCryptPasswordEncoder passwordEncoder(){
        //encripta el pass, este tipo de cifrado no da el mismo pass
        //aunque sea el mismo sin cifrar
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> {
                            authz.requestMatchers("/").permitAll()
                                    .requestMatchers("/registration").permitAll()
                                    .requestMatchers("/recuperar_contrasena").permitAll()
                                    .requestMatchers("/recuperar_contrasena/**").permitAll()
                                    .requestMatchers("/home/**").hasAnyRole("USER", "ADMIN")
                                    .anyRequest().authenticated();
                        }
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}

