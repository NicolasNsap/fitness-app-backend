package com.fitnessapp.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter - El Guardia de Seguridad
 *
 * Intercepta TODAS las peticiones HTTP antes de llegar a los Controllers.
 *
 * Flujo:
 * 1. Extrae el token del header "Authorization: Bearer xxx"
 * 2. Válida el token usando JwtService
 * 3. Si es válido, establece la autenticación en SecurityContext
 * 4. Continúa con la cadena de filtros
 *
 * @author Nicolas Abarca
 */

@Component//spring lo administra automáticamente
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter quiere decir ejecutarse una vez por petición

    private final JwtService jwtService;

    //constructor para la inyección de la dependencia JwtService
    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        //busca el header "autorización" en la petición HTTp
        final String authHeader = request.getHeader("Authorization");

        //verificación del formato del header
        //si no hay header o no empieza con "Bearer", continua sin autenticar
        //ejemplo header válido: "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
        if (authHeader == null || !authHeader.startsWith("Bearer ")){//si cualquiera de estas es true no hay token válido
            filterChain.doFilter(request, response);//continua si autenticar
            return;//sale del método
        }

        //extraer el token
        final String token = authHeader.substring(7);//quita los primeros 7 caracteres `"Bearer "` para obtener solo el token

        //validar el token y establecer autenticación
        try {
            //validar que el token sea valido (firma correcta, no expirado)
            if (jwtService.validateToken(token)){
                //extraer información del token osea lee el payload del token y extrae los datos
                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                //verificar si ya hay autenticación
                if (SecurityContextHolder.getContext().getAuthentication() == null){

                    //crear objeto que representa el usuario autenticado
                    //ROLE_ es el prefijo que Spring Security espera
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                    //agregar detalles de la petición como la dirección ip del cliente id de la sesion otros metadatos
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //establecer autenticación en el contexto, Guarda la autenticación en el **SecurityContext**.
                    //ahora spring security sabe quien es el usuario
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch (Exception e){
            //si hay error, no se autentica
            //la petición continua sin autenticación
            System.out.println("Error procesando token: " + e.getMessage());
        }

        //continuar la cadena de filtros en este caso pasa al controller
        //importante -> siempre llamar a filterChain.doFilter() si no lo hacemos la petición se queda atascada
        filterChain.doFilter(request, response);


    }

}
