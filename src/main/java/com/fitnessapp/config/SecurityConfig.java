package com.fitnessapp.config;

import com.fitnessapp.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    //inyeccion de JwtAuthenticationFilter
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * configura la cadena de filtros de seguridad, es como definir las reglas de acceso de un edificio
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                //se deshabilita CSRF
                //(Cross-Site Request Forgery) es una protección para app erb tradicionales con formularios
                //con JWT no lo necesitamos, ya que no usamos cookies de sesión, el token viaja en el header, cada petición es independiente (stateless)
                .csrf(csrf -> csrf.disable())

                /**
                 * Configurar sesiones como STATELESS
                 * se le dice a spring que no guarde sesiones del usuario
                 * porque se usara JWT por lo que cada peticion trae su propio token, el servidor no necesita recordar al usuario, cualquier servidor puede validar el token
                 */
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //configurar reglas de autorizacion
                .authorizeHttpRequests(auth -> auth
                        //rutas públicas, no requieren token
                        .requestMatchers("/api/auth/**").permitAll()

                        //RUTAS DE EXERCISES

                        //GET: cualquier usuario autenticado puede ver ejercicios
                        .requestMatchers(HttpMethod.GET, "/api/exercises/**").authenticated()

                        //POST, PUT, DELETE: Solo ADMIN puede modificar ejercicios
                        .requestMatchers(HttpMethod.POST, "/api/exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exercises/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exercises/**").hasRole("ADMIN")

                        //RUTAS DE USERS

                        //GET usuarios: solo Admin puede ver la lista de usuarios
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")

                        //GET mi perfil: cualquier autenticado puede ver su perfil
                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()

                        //PUT, DELETE usuarios: Solo ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        //cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                    )
                    //AGREGAR FILTRO JWT A LA CADENA
                    // Ubicamos nuestro JwtAuthenticationFilter ANTES del filtro
                    // de autenticación estándar de Spring.
                    //
                    // Orden: Request → JwtFilter → UsernamePasswordFilter → Controller
                    //
                    // Así, cuando la petición llegue a las reglas de autorización,
                    // el SecurityContext ya tendrá la autenticación establecida
                    // (si el token era válido).
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class

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
