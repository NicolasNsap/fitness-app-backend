package com.fitnessapp.service;

import com.fitnessapp.dto.request.LoginRequestDTO;
import com.fitnessapp.dto.request.RegisterRequestDTO;
import com.fitnessapp.dto.response.AuthResponseDTO;
import com.fitnessapp.entity.Role;
import com.fitnessapp.entity.User;
import com.fitnessapp.exception.BadRequestException;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.repository.RoleRepository;
import com.fitnessapp.repository.UserRepository;
import com.fitnessapp.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AUTH SERVICE - La Lógica de Autenticación
 *
 * maneja:
 * registro de nuevos usuarios
 * login y generacion del token JWT
 *
 * @author Nicolas Abarca
 */
@Service
public class AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //constructor - Spring inyecta las dependencias automáticamente
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registra un nuevo usuario
     *
     * Flujo:
     * 1. Verificar que username no exista
     * 2. Verificar que email no exista
     * 3. Buscar el rol USER en la BD
     * 4. Hashear contraseña con BCrypt
     * 5. Guardar usuario en BD
     * 6. Generar token JWT
     * 7. Retornar respuesta con token
     *
     * @param request Datos del registro (username, email, password)
     * @return AuthResponseDTO con el token JWT
     */
    public AuthResponseDTO register(RegisterRequestDTO request) {

        //verificar que el username no exista
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El username ya está en uso");
        }

        //verificar que el email no exista
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        //buscar el rol USER en la base de datos
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Rol USER no encontrado en la BD"));

        //crear el nuevo usuario
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))  // Hashear contraseña
                .role(userRole)  // Asignar entidad Role
                .build();

        //guardar en BD
        User savedUser = userRepository.save(newUser);

        //generar token JWT (pasamos el nombre del rol como String)
        String token = jwtService.generateToken(
                savedUser.getUsername(),
                savedUser.getRole().getName()  // "USER" o "ADMIN"
        );

        //construir y retornar respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roleName(savedUser.getRole().getName())  // Nombre del rol
                .build();
    }

    /**
     * Autentica un usuario y genera token JWT
     *
     * Flujo:
     * 1. Buscar usuario por username o email
     * 2. Verificar contraseña con BCrypt
     * 3. Generar token JWT
     * 4. Retornar respuesta con token
     *
     * @param request Datos del login (identifier, password)
     * @return AuthResponseDTO con el token JWT
     */
    public AuthResponseDTO login(LoginRequestDTO request) {

        //buscar usuario por username o email
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //verificar que el usuario esté activo
        if (!user.isActive()) {
            throw new BadRequestException("La cuenta está desactivada");
        }

        //verificar contraseña (comparar con passwordHash)
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Contraseña incorrecta");
        }

        //generar token JWT (pasamos el nombre del rol como String)
        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().getName()  // "USER" o "ADMIN"
        );

        //construir y retornar respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole().getName())  // Nombre del rol
                .build();
    }
}
