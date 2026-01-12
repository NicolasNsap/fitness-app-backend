package com.fitnessapp.service;

import com.fitnessapp.dto.request.UserRequestDTO;
import com.fitnessapp.dto.response.UserResponseDTO;
import com.fitnessapp.entity.Role;
import com.fitnessapp.entity.User;
import com.fitnessapp.mapper.UserMapper;
import com.fitnessapp.repository.RoleRepository;
import com.fitnessapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Logica de negocio para operaciones de usuarios
 *
 * Responsabilidades
 * -Registro de nuevos usuarios
 * -validaciones de negocios
 * -transformacion de DTO <-> Entity
 * -Encriptacion de passwords
 * -busqueda y listado de usuarios
 */

@Service //marca esta clase como un servicio de spring
@RequiredArgsConstructor //inyección por constructor
@Slf4j //logger automático
public class UserService {

    //dependencias inyectadas automáticamente por constructor
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; //Bcrypt
    //private final JwtService jwtService; //lo creamos después

    /**
     * Registra un nuevo usuario en el sistema
     * flujo:
     * -> válida que username y email no existen
     * -> convierte DTO a Entity
     * -> Encripta la contraseña
     * -> asigna rol USER por defecto
     * -> guarda en BD
     * -> convierte Entity a ResponseDTO
     *
     * @Transaccional -> si algo falla, todo se revierte (rollback)
     * @param request DTO con datos del nuevo usuario
     * @return ResponseDTO con datos del usuario creado
     * @throws RuntimeException si username o email ya existen
     */
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO request){//registra un usuario request, retorna una respuesta responseDTO
        log.info("Iniciando registro de usuario: {}", request.getUsername());

        //validación 1 username único
        if (userRepository.existsByUsername(request.getUsername())){//al método existsByUsername del userRepository verifica si el username ya existe
            log.warn("Intento de registro con username existente: {}", request.getUsername());
            throw new RuntimeException("El username ya existe");
        }

        //validación 2 email único
        if (userRepository.existsByEmail(request.getEmail())){//al método existsByEmail del userRepository verifica si el email ya existe
            //si el email ya está registrado en la base de datos
            log.warn("Intento de registro con email existente: {}", request.getEmail());// se le advierte que el email ya existe
            throw new RuntimeException("El email ya esta en uso");//se lanza una exception en tiempo de ejecución
        }

        //paso 1 convertir DTO a Entity (parcial)
        User user = userMapper.toEntity(request);//los datos ingresados por el usuario recepcionados como resquesDTO ControlSegDatos se convierten a datos de entity para
                                                //ser enviados a la  base de datos

        //paso 2 encriptar contraseña
        String encodedPassword = passwordEncoder.encode(request.getPassword());//en la variable encodedPasswords se almacena  el password encriptado
        user.setPasswordHash(encodedPassword);//es password encriptado se setea en el setPasswordHash de la entidad
        log.debug("Contraseña encriptada exitosamente");

        //paso 3 asignar rol USER por defecto
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.setRole(userRole);//se le asigna un rol al registro que por defecto hasta momento será USER

        //paso 4 guardar en BD
        User savedUser = userRepository.save(user);
        log.info("Usuario registrado exitosamente con ID: {}", savedUser.getId());

        //paso 5 convertir Entity a ResponseDTO
        return userMapper.toResponseDTO(savedUser);//los datos del registro se convierten a responseDTO por seguridad de datos y se retornan como respuesta de registro exitoso al usuario
    }

    /**
     * buscar usuario por si ID
     * @param id ID del usuario
     * @return ResponseDTO del usuario
     * @throws RuntimeException si no existe
     */
    @Transactional(readOnly = true)//read only solo lectura osea que no se puede modificar
    public UserResponseDTO findById(UUID id){
        log.info("Buscando usuario con ID: {}", id);

        User user = userRepository.findById(id)//a la entidad user se le almacena el id encontrando en la base de datos
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));//sino encuentra el id, lanza una excepción

        //el metodo toResponseDTO de la clase userMapper se le pasa como parámetro el user que se encontró con ese ID para ser mostrado al usuario como respuesta
        return userMapper.toResponseDTO(user);
    }

    /**
     * busca un usuario por su username
     *
     * @param username Username del usuario
     * @return ResponseDTO del usuario
     * @throws RuntimeException si no existe
     */
    @Transactional(readOnly = true)
    public UserResponseDTO findByUsername(String username) {
        log.info("Buscando usuario: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        return userMapper.toResponseDTO(user);
    }

    /**
     * Lista todos los usuarios del sistema
     * Solo ADMIN debería poder llamar este metodo (se validará en security)
     *
     * @return lista ResponseDTO de todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers(){
        log.info("Listando todos los usuarios");

        return userRepository.findAll()
                //convertir a stream
                .stream()//stream es como una cinta transportadora que procesa los elementos uno por uno
                //forma larga mediante lambda: .map(user -> userMapper.toResponseDTO(user)
                .map(userMapper::toResponseDTO)//map() transforma cada elemento del sistema toma un user y lo convierte a UserResponse DTO
                //recolectar en una lista
                .collect(Collectors.toList());//collect() toma todos los elementos del stream los reune en una estructura de datos, en este caso una List

    }

    /**
     * Actualiza información de un usuario
     * @param  id del usuario a actualizar
     * @param request datos nuevos
     * @return Usuario actualizado
     */
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO request){
        log.info("Actualizando usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        //actualizar solo campos permitidos (no username ni email por ahora)

        //sí cambia la contraseña, encriptarla
        if(request.getPassword() != null && !request.getPassword().isEmpty()){
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updateUser = userRepository.save(user);
        log.info("Usuario actualizado exitosamente: {}", updateUser.getUsername());

        return userMapper.toResponseDTO(updateUser);
    }

    /**
     * Desactiva el usuario (soft delete)
     * No lo borra físicamente, solo marca isActive = false
     *
     * @param id del usuario a desactivar
     */
    @Transactional
    public void desactivateUser(UUID id){
        log.info("Desactivando usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        user.setIsActive(false);
        userRepository.save(user);

        log.info("Usuario desactivado: {}", user.getUsername());
    }

    /**
     * NOTA: El método de LOGIN lo implementaremos después
     * cuando tengamos JwtService configurado
     *
     * public AuthResponseDTO login(LoginRequestDTO request) { ... }
     */
}
