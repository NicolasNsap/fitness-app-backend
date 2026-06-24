package com.fitnessapp.mapper;

import com.fitnessapp.dto.response.RoutineExerciseResponseDTO;
import com.fitnessapp.dto.response.RoutineResponseDTO;
import com.fitnessapp.entity.Routine;
import com.fitnessapp.entity.RoutineExercise;
import org.springframework.stereotype.Component;

/**
 * RoutineMapper - convierte la rutina sacada desde la base de datos a responseDTO que ve el usuario
 */
@Component//para que spring administre la clase crea una instancia automáticamente lista para inyectar
public class RoutineMapper {

    //método para convertir un ejercicio de la rutina a responseDTO
    public RoutineExerciseResponseDTO toRoutineExerciseResponseDTO(RoutineExercise routineExercise) {
        return RoutineExerciseResponseDTO.builder()
                .id(routineExercise.getId())
                .exerciseId(routineExercise.getExercise().getId())
                .exerciseName(routineExercise.getExercise().getName())
                .sets(routineExercise.getSets())
                .targetReps(routineExercise.getTargetReps())
                .restSeconds(routineExercise.getRestSeconds())
                .orderIndex(routineExercise.getOrderIndex())
                .notes(routineExercise.getNotes())
                .build();
    }

    //método para convertir una rutina obtenida de la base de datos a DTO por eso retorna un DTO
    public RoutineResponseDTO toRoutineResponseDTO(Routine routine){
        return RoutineResponseDTO.builder()
                .id(routine.getId())
                .name(routine.getName())
                .durationMinutes(routine.getDurationMinutes())
                .notes(routine.getNotes())
                .routineExercises(routine.getRoutineExercises().stream().map(this::toRoutineExerciseResponseDTO).toList())
                .createdAt(routine.getCreatedAt())
                .build();
    }
}
