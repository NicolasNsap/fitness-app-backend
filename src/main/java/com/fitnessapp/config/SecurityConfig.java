package com.fitnessapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /**
     * Se define el PasswordEncoder como un Bean de spring
     *
     * @Bean: le dice a spring que cree este objeto y lo administre
     * spring lo inyectara automáticamente donde se necesite
     *
     * BCryptPasswordEncoder:
     * -Implementacion de PasswordEncoder que esa BCrypt
     * -Por defecto usa strength=10 (2^10 = 1,024 iteraciones)
     * strength: se refiere al número de rondas de hashing hace el proceso más lento intencionalmente para dificultar ataques de fuerza bruta
     * -Genenera salt(valor aleatorio único que se agrega a la contraseña antes de hashearla asea que dos usuarios pueden tener la misma contraseña,
     * pero no se genera el mismo hash) automático
     * - Es thread-safe (puede usarse concurrentemente) quiere decir que multiples hilos pueden usar la misma instancia de BCryptPasswordEncoder sin problemas
     *
     * como funciona la inyección
     * #spring ve @Bean
     * #Crea una instancia de BCryptPasswordEncoder
     * #La guarda en su contenedor (ApplicationContext)
     * #Cuando UserService pide un PasswordEncoder,
     * Spring le da esta instancia
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {//PasswordEncoder es una interfaz de SpringSecurity que define el contrato para encryptar contraseñas
        //la implementación que se usara es BCrypt, ya que tiene buen balance entre seguridad y rendimiento
        return new BCryptPasswordEncoder();
    }

}
