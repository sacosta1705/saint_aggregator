package com.saintnet.aggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Permite el acceso a la consola H2 (si la usas para dev) y recursos estáticos
                // .requestMatchers("/h2-console/**").permitAll() // Si usas H2 para desarrollo
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // Por ahora, permite todo en /admin-tiendas. Luego puedes restringirlo.
                .requestMatchers("/admin-tiendas/**").permitAll() 
                // Puedes requerir autenticación para otras rutas en el futuro
                .anyRequest().authenticated() 
            )
            .formLogin(withDefaults()) // Configuración de login por defecto (si lo necesitas)
            .csrf(csrf -> csrf
                // .ignoringRequestMatchers("/h2-console/**") // Si usas H2
                // Puedes necesitar deshabilitar CSRF para ciertas APIs o configurarlo adecuadamente
                // Por ahora, para simplificar el admin local, podríamos deshabilitarlo para /admin-tiendas
                // o manejar tokens CSRF en tus formularios Thymeleaf.
                // Para una interfaz web local que solo tú usas, el riesgo es menor.
                .disable() // DESHABILITAR CSRF TEMPORALMENTE PARA SIMPLIFICAR EL ADMIN. REVISAR EN PRODUCCIÓN.
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // Para H2 console si se usa
        return http.build();
    }
}