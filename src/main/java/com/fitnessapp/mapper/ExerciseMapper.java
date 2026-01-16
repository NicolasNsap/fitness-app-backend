package com.fitnessapp.mapper;

import com.fitnessapp.dto.request.ExerciseRequestDTO;
import com.fitnessapp.dto.response.ExerciseResponseDTO;
import com.fitnessapp.entity.Exercise;
import org.springframework.stereotype.Component;

/**
 * ExerciseMapper Clase utilitaria para transformar entre Entity y DTO
 * similar a UserMapper pero para ejercicios
 *
 * @Component Spring la administra como bean inyectable
 */
@Component
public class ExerciseMapper {

    /**
     * Convierte ExerciseResquestDTO a Exercise(Entity)
     *
     * esta conversion es completa y directa
     * no requiere logica adicional como userMapper (sin encriptacion, sin roles)
     *
     * @param requestDTO DTO con datos del request
     * @return Entity Exercise sein giardar (aun no tiene ID
     */
    public Exercise toEntity(ExerciseRequestDTO requestDTO){
        return Exercise.builder()
                .name(requestDTO.getName())
                .muscleGroup(requestDTO.getMuscleGroup())
                .equipmentNeeded(requestDTO.getEquipmentNeeded())
                .difficultyLevel(requestDTO.getDifficultyLevel())
                .videoUrl(requestDTO.getVideoUrl())
                .isActive(true)//por defecto activo
                .build();

    }

    /**
     * Convierte Exercise (Entity) a ExerciseResponseDTO
     *
     * esta conversion es completa y segura
     * incluye todos los campos públicos del ejercicio
     *
     * @param exercise Entity Exercise de la BD
     * @return DTO listo para enviar la cliente
     */
    public ExerciseResponseDTO toResponseDTO(Exercise exercise){
        return ExerciseResponseDTO.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .muscleGroup(exercise.getMuscleGroup())
                .equipmentNeeded(exercise.getEquipmentNeeded())
                .difficultyLevel(exercise.getDifficultyLevel())
                .mediaUrl(exercise.getVideoUrl())
                .isActive(exercise.isActive())
                .build();
    }

    /**
     * Actualiza un Exercise existente con datos de un DTO
     *
     * Útil para operaciones de UPDATE donde ya tienes la entidad
     * y solo quieres actualizar sus campos sin crear una nueva instancia
     *
     * @param exercise Entity existente a actualizar
     * @param exerciseRequestDTO DTO con los nuevos datos
     */
    public void updateEntityFromDTO(Exercise exercise, ExerciseRequestDTO exerciseRequestDTO) {
        exercise.setName(exerciseRequestDTO.getName());
        exercise.setMuscleGroup(exerciseRequestDTO.getMuscleGroup());
        exercise.setEquipmentNeeded(exerciseRequestDTO.getEquipmentNeeded());
        exercise.setDifficultyLevel(exerciseRequestDTO.getDifficultyLevel());
        exercise.setVideoUrl(exerciseRequestDTO.getVideoUrl());
    }
}
