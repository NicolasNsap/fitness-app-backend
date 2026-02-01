package com.fitnessapp.controller;

import com.fitnessapp.dto.request.ExerciseRequestDTO;
import com.fitnessapp.dto.response.ExerciseResponseDTO;
import com.fitnessapp.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Maneja peticiones HTTP relacionadas con ejercicios
 *
 * Endpoint:
 * - GET /api/exercises → Listar todos los ejercicios activos
 * - GET /api/exercises → Listar todos los ejercicios activos
 * - PUT /api/exercises/{id} -> actualizar datos de un ejercicio existente
 * - DELETE /api/exercises/{id} -> desactivar ejercicio del catálogo
 */
@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;

    /**
     * Registra un nuevo ejercicio en el sistema
     */
    @PostMapping("/register")
    public ResponseEntity<ExerciseResponseDTO> registerExercise(@Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO){
        log.info("Peticion de registro recibida para ejercicio: {}", exerciseRequestDTO.getName());

        //se llama al servicio
        ExerciseResponseDTO newExercise = exerciseService.createExercise(exerciseRequestDTO);

        log.info("Ejercicio registrado exitosamente con ID: {}", newExercise.getId());

        //retornar respuesta con status 201 Created
        return new ResponseEntity<>(newExercise, HttpStatus.CREATED);

    }

    /**
     * Listar todos los ejercicios activos
     *
     * @return ResponseEntity con lista de ejercicios y status 200
     */
    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getAllExercises(){
        log.info("Peticion para listar todos los ejercicios activos");

        //llamar al servicio
        List<ExerciseResponseDTO> exercises = exerciseService.findAllActiveExercises();

        log.info("Se encontraron {} ejercicios activos", exercises.size());

        //retornar respuesta status 200 OK
        return ResponseEntity.ok(exercises);
    }


    /**
     * Obtiene un ejercicio por su ID
     *
     * Endpoint: GET /api/exercises/{id}
     *
     * Ejemplo: GET /api/exercises/1
     *
     * Response (200 OK):
     * {
     *   "id": 1,
     *   "name": "Sentadilla",
     *   "description": "Ejercicio compuesto para piernas...",
     *   "muscleGroup": "PIERNAS",
     *   "equipmentNeeded": "Barra",
     *   "difficultyLevel": "INTERMEDIO",
     *   "isActive": true
     * }
     *
     * Response (404 Not Found) si no existe:
     * {
     *   "status": 404,
     *   "error": "Not Found",
     *   "message": "Ejercicio no encontrado con id: 999",
     *   ...
     * }
     *
     * @param id  del ejercicio a buscar
     * @return ResponseEntity con ExerciseResponseDTO y status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable UUID id) {

        log.info("Petición para obtener ejercicio con ID: {}", id);

        //llamar al Service
        ExerciseResponseDTO exercise = exerciseService.findById(id);

        log.info("Ejercicio encontrado: {}", exercise.getName());

        // retornar respuesta con status 200 OK
        return ResponseEntity.ok(exercise);
    }

    /**
     * Actualizar datos
     * ENDPOINT: PUT /api/exercises/{id}
     *
     * - este ENDPOINT lo puede usar solo el admin
     *
     * Request Body (JSON):
     * {
     *   "name": "Press Banca",
     *   "muscleGroup": "PECHO", ← Corregido
     *   "equipmentNeeded": "Barra y banco",
     *   "difficultyLevel": "INTERMEDIO",
     *   "videoUrl": "https://youtube.com/..."
     * }
     * Response (200 OK):
     * {
     *   "id": "uuid-del-ejercicio",
     *   "name": "Press Banca",
     *   "muscleGroup": "PECHO",
     *   "equipmentNeeded": "Barra y banco",
     *   "difficultyLevel": "INTERMEDIO",
     *   "mediaUrl": "https://youtube.com/...",
     *   "isActive": true
     * }
     *
     * @param id del ejercicio a actulizar
     * @param exerciseRequestDTO nuevos datos del ejercicio
     * @return Ejercicio actualizado con status 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(@PathVariable UUID id, @Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO){
        log.info("Petición para actualizar ejercicio con ID: {}", id);

        //llamada al servicio
        ExerciseResponseDTO updateExercise = exerciseService.updateExercise(id, exerciseRequestDTO);

        log.info("Ejercicio actualizado exitosamente: {}", updateExercise.getName());

        //se retorna respuesta con status 200 OK
        return ResponseEntity.ok(updateExercise);
    }

    /**
     * Desactivar ejercicio
     * ENDPOINT: DELETE /api/exercises/{id}
     *
     * - Se guarda en la base de datos, pero en modo desactivado
     * - no aparece en le catalogo público
     * - el historial se mantiene intacto
     * - si algún día la necesito se puede reactivar
     *
     * Response (204 No Content):
     * - Sin body (el ejercicio fue desactivado exitosamente)
     *
     * @param id del ejercicio a desactivar
     * @return ResponseEntity vacío con status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable UUID id){
        log.info("Petición para desactivar ejercicio con ID: {}", id);

        //llamada el servicio
        exerciseService.desactivateExercise(id);

        log.info("Ejercicio con ID: {} desactivado exitosamente", id);

        // Retornar 204 No Content
        return ResponseEntity.noContent().build();
    }
}
