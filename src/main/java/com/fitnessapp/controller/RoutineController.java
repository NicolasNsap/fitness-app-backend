package com.fitnessapp.controller;

import com.fitnessapp.dto.request.CreateRoutineRequestDTO;
import com.fitnessapp.dto.response.RoutineResponseDTO;
import com.fitnessapp.dto.response.WorkoutResponseDTO;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.repository.UserRepository;
import com.fitnessapp.service.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@Slf4j
public class RoutineController {
    private final RoutineService routineService;
    private final UserRepository userRepository;

    /**
     * METODO PRIVADO PARA OBTENER EL ID DEL SUSUARIO CON DATOS DEL JWT
     * @param authentication
     * @return
     */
    private UUID getUserIdFromAuth(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"))
                .getId();

    }

    /**
     *METOOD PARA CREAR UNA RUTINA
     * @param requestDTO
     * @param authentication
     * @return
     */
    @PostMapping
    public ResponseEntity<RoutineResponseDTO> createRoutine(@Valid @RequestBody CreateRoutineRequestDTO requestDTO, Authentication authentication){
        //se obtiene el ID del método que extrae el id desde el token JWT
        UUID userId = getUserIdFromAuth(authentication);
        //la rutina creada se convierte en un responseDTO
        RoutineResponseDTO responseDTO = routineService.createRoutine(userId, requestDTO);
        //se retorna la respuesta con la rutina creada con el estatus CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    /**
     * MOSTRAR TODAS LAS RUTINAS DE UN USUARIO
     *
     * @param authentication
     * @return
     */
    @GetMapping
    public ResponseEntity<List<RoutineResponseDTO>> getUserRoutines(Authentication authentication) {
        //se obtiene el ID del método que extrae el id desde el token JWT
        UUID userId = getUserIdFromAuth(authentication);
        //se obtienen las rutinas y se almacenan en una lista
        List<RoutineResponseDTO> responseDTO = routineService.getUserRoutines(userId);
        //se retorna la respuesta con la lista de las rutinas
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * MOSTRAR UNA RUTINA DE UN USUARIO
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoutineResponseDTO> getRoutineById(@PathVariable UUID id, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        RoutineResponseDTO responseDTO = routineService.getRoutineById(id, userId);


        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * ELIMINAR RUTINA
     * @param id
     * @param authentication
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable UUID id, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        routineService.deleteRoutine(id, userId);

        return ResponseEntity.noContent().build();
    }
}
