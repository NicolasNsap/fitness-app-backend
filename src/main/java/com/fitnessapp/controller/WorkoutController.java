package com.fitnessapp.controller;

import com.fitnessapp.dto.request.CreateWorkoutExerciseRequestDTO;
import com.fitnessapp.dto.request.CreateWorkoutRequestDTO;
import com.fitnessapp.dto.request.UpdateWorkoutRequestDTO;
import com.fitnessapp.dto.response.WorkoutExerciseResponseDTO;
import com.fitnessapp.dto.response.WorkoutResponseDTO;
import com.fitnessapp.entity.User;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.repository.UserRepository;
import com.fitnessapp.service.WorkoutService;
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
@RequestMapping("/api/workouts")//Ruta base
@RequiredArgsConstructor
@Slf4j
public class WorkoutController {
    private final WorkoutService workoutService;
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
     * CREAR UN ENTRENAMIENTO
     * @param request
     * @param authentication
     * @return
     */
    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> createWorkout(@Valid @RequestBody CreateWorkoutRequestDTO request, Authentication authentication) {


        UUID userId = getUserIdFromAuth(authentication);

        WorkoutResponseDTO response = workoutService.createWorkout(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * MOSTRAR TODOS LOS ENTRENAMIENTOS DE UN USUARIO
     *
     * @param authentication
     * @return
     */
    @GetMapping
    public ResponseEntity<List<WorkoutResponseDTO>> getMyWorkouts(Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        List<WorkoutResponseDTO> responseDTO = workoutService.getUserWorkouts(userId);

        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * MOSTRAR UN ENTRENAMIENTO DE UN USUARIO
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> getWorkoutById(@PathVariable UUID id, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        WorkoutResponseDTO responseDTO = workoutService.getWorkoutById(id, userId);


        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * ACTUALIZAR UN ENTRENAMIENTO
     * @param id
     * @param request
     * @param authentication
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> updateWorkout(@PathVariable UUID id, @Valid @RequestBody UpdateWorkoutRequestDTO request, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        WorkoutResponseDTO responseDTO = workoutService.updateWorkout(id, userId, request);

        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * ELIMINAR ENTRENAMIENTO
     * @param id
     * @param authentication
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable UUID id, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        workoutService.deleteWorkout(id, userId);

        return ResponseEntity.noContent().build();
    }

    /**
     * CONFIRMAR ENTRENAMIENTO COMPLETO
     * @param id
     * @param authentication
     * @param durationMinutes
     * @return
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<WorkoutResponseDTO> completeWorkout(@PathVariable UUID id, Authentication authentication, @RequestParam(required = false) Integer durationMinutes) {

        UUID userId = getUserIdFromAuth(authentication);

        WorkoutResponseDTO responseDTO = workoutService.completeWorkout(id, userId, durationMinutes);

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/{id}/exercises")
    public ResponseEntity<WorkoutResponseDTO> addExerciseToWorkout(@PathVariable UUID id, Authentication authentication, @Valid @RequestBody CreateWorkoutExerciseRequestDTO requestDTO) {

        UUID userId = getUserIdFromAuth(authentication);

        WorkoutResponseDTO responseDTO = workoutService.addExerciseToWorkout(id, userId, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
