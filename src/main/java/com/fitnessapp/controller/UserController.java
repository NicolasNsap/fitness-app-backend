package com.fitnessapp.controller;

import com.fitnessapp.dto.request.UserRequestDTO;
import com.fitnessapp.dto.response.ExerciseResponseDTO;
import com.fitnessapp.dto.response.UserResponseDTO;
import com.fitnessapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Maneja peticiones HTTP relacionadas con usuarios
 *
 * Endpoints:
 * POST /api/users/register -> registrar usuario
 * GET /api/users/ -> mostrar todos los usuarios
 * GET /api/users/{id} -> mostrar usuario por su id
 * PUT /api/users/{id} -> actualizar datos de usuario existente
 * DELETE /api/users/{id} -> para este caso se desactivará un usuario(no borra físicamente
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Registra un nuevo usuario en el sistema
     * Endpoint: POST /api/users/register
     * Request Body (JSON):
     * {
     *   "username": "juan",
     *   "email": "juan@example.com",
     *   "password": "123456",
     *   "firstName": "Juan",
     *   "lastName": "Pérez"
     * }
     * Response (201 Created):
     * {
     *   "id": 1,
     *   "username": "juan",
     *   "email": "juan@example.com",
     *   "firstName": "Juan",
     *   "lastName": "Pérez",
     *   "roleName": "USER",
     *   "isActive": true,
     *   "createdAt": "2025-01-16T18:00:00"
     * }
     *
     * @param userRequestDTO con los datos del nuevo usuario
     * @return ResponseEntity con UserResponseDTO y status 201
     */
    @PostMapping("/register")//url completa http://localhost:8080/api/users/register
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO){

        log.info("Peticion de registro recibida para username: {}", userRequestDTO.getUsername());

        //se llama al servicio
        UserResponseDTO newUser = userService.registerUser(userRequestDTO);

        log.info("Usuario registrado exitosamente con ID: {}", newUser.getId());

        //retornar respuesta con status 201 Created
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * obtener todos los uauarios
     *
     * el endpoint seria igual al al metodo de buscar usuario pr id solo  que mustra todo los usuarios
     *
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        log.info("Peticion para obtener todos los usuarios");

        List<UserResponseDTO> userResponseDTOS = userService.findAllUsers();

        return ResponseEntity.ok(userResponseDTOS);
    }

    /**
     * Obtener usuario por ID
     *
     * Endpoint: GET /api/users/{id}
     * {
     *  "id": 1,
     *  "username": "juan",
     *  "email": "juan@example.com",
     *  "firstName": "Juan",
     *  "lastName": "Pérez",
     *  "roleName": "USER",
     *  "isActive": true,
     *  "createdAt": "2025-01-16T18:45:30"
     * }
     * Response (404 Not Found) si no existe:
     * {
     *   "status": 404,
     *   "error": "Not Found",
     *   "message": "Usuario no encontrado con id: 999",
     *   "path": "/api/users/999",
     *   "details": {
     *     "resourceName": "Usuario",
     *     "fieldName": "id",
     *     "fieldValue": "999"
     *   }
     * }
     *
     * @param id del usuario que se busca
     * @return ResponseEntity con UserResponseDTO y status code 200
     */
    @GetMapping("/{id}")//ruta específica del metodo
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id){
        log.info("Peticion para obtener usuario con ID: " + id);

        //llamar al servicio
        UserResponseDTO userResponseDTO = userService.findById(id);

        log.info("Usuario encontrado: {}", userResponseDTO.getUsername());

        //retorna respuesta con status 200 ok
        return ResponseEntity.ok(userResponseDTO);//este es un atajo para return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Actuallizar los datos de un usuario
     * ENDPOINT: PUT /api/users/{id}
     * Request Body (JSON):
     * {
     *   "username": "nicolas_updated" -> Nuevo username (opcional)
     *   "email": "nuevo@email.com" -> Nuevo email (opcional)
     *   "password": "nuevaPassword123" -> Nueva contraseña (opcional)
     * }
     * Response (200 OK):
     * {
     *   "id": "uuid-del-usuario",
     *   "username": "nicolas_updated",
     *   "email": "nuevo@email.com",
     *   "roleName": "USER",
     *   "isActive": true,
     *   "updatedAt": "2025-02-01T15:30:00" -> Nota: fecha actualizada
     * }
     *
     * @param id del usuario a actualizar
     * @param userRequestDTO nuevos datos del usuario
     * @return Usuario actualizado con status 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDTO userRequestDTO){

        log.info("Petición para actualizar usuario con ID: {}", id);

        //llamar al servicio
        UserResponseDTO updateUser = userService.updateUser(id, userRequestDTO);

        log.info("Usuario actualizado exitosamente: {}", updateUser.getUsername());

        //se retorna respuesta con status 200 ok
        return ResponseEntity.ok(updateUser);
    }

    /**
     * Desactivar un usuario
     * DELETE /api/users/{id}
     *
     * porque desactivar (soft delete) envez de borrar
     * - porque evita borrar datos por error
     * - se mantiene el historial de quien existió
     * - el usuario puede volver
     * - si el usuario tiene entrenamientos registrados, borrar causaria errores en otras tablas
     *
     * Response (204 No Content):
     * - No retorna body (el usuario fue desactivado, no hay nada que mostrar)
     * - Status 204 significa "OK, lo hice, pero no tengo nada que devolverte"
     *
     * Alternativa común: Retornar 200 con mensaje
     * { "message": "Usuario desactivado exitosamente" }
     *
     * @param id del usuario a desactivar
     * @return ResponseEntity vacío con status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        log.info("Petición para desactivar usuario con ID: {}", id);

        //llamar al servicio
        userService.desactivateUser(id);

        log.info("Usuario con ID: {} desactivado exitosamente", id);

        // Retornar 204 No Content
        // .noContent() = status 204
        // .build() = construye la respuesta sin body
        return ResponseEntity.noContent().build();
    }

}
