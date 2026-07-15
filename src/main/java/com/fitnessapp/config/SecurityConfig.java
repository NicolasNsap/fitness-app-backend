package com.fitnessapp.config;

import com.fitnessapp.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    //inyeccion de JwtAuthenticationFilter
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //constructor
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //rutas públicas, no requieren token
                        .requestMatchers("/api/auth/**").permitAll()

                        // Swagger - documentación pública
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()

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

    /**
     * Este Bean es necesario para que la clase AuthService pueda verificar el usuario y contraseña en el login
     * @param config
     * @return
     * @throws Exception
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    /**
     * crea una configuración de spring que spring usara para CORS
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        //significa que acepta peticiones de cualquier origen, para producción poner solo el dominio
        configuration.setAllowedOrigins(List.of("*"));
        //que métodos HTTP acepta, options es necesario porque el navegador lo envía antes de cada petición para preguntar permiso
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        //acepta cualquier header, incluyendo Authorization con el JWT
        configuration.setAllowedHeaders(List.of("*"));
        //permite que el frontend lea el header Authorization de las respuestas
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //aplica esta configuración a todas las rutas
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

}
