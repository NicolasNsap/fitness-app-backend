package com.fitnessapp.mapper;

import com.fitnessapp.dto.response.SetResponseDTO;
import com.fitnessapp.dto.response.WorkoutExerciseResponseDTO;
import com.fitnessapp.dto.response.WorkoutResponseDTO;
import com.fitnessapp.entity.ExerciseSet;
import com.fitnessapp.entity.Workout;
import com.fitnessapp.entity.WorkoutExercise;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * WORKOUT MAPPER
 *
 * Convierte entre entidades y DTOs.
 * Centraliza la lógica de transformación.
 *
 * @author Nicolas Abarca
 */
@Component//para que spring administre esta clase
public class WorkoutMapper {

    /**
     * Convierte ExerciseSet a SetResponseDTO
     */
    public SetResponseDTO toSetResponseDTO(ExerciseSet set) {
        return SetResponseDTO.builder()
                .id(set.getId())
                .setNumber(set.getSetNumber())
                .setType(set.getSetType())
                .weight(set.getWeight())
                .reps(set.getReps())
                .rir(set.getRir())
                .rpe(set.getRpe())
                .restSeconds(set.getRestSeconds())
                .completed(set.getCompleted())
                .notes(set.getNotes())
                .volume(set.getVolume())  //método calculado en la entidad
                .build();
    }

    /**
     * Convierte WorkoutExercise a WorkoutExerciseResponseDTO
     */
    public WorkoutExerciseResponseDTO toWorkoutExerciseResponseDTO(WorkoutExercise workoutExercise) {
        List<SetResponseDTO> sets = workoutExercise.getSets().stream()
                .map(this::toSetResponseDTO)
                .toList();

        return WorkoutExerciseResponseDTO.builder()
                .id(workoutExercise.getId())
                .orderIndex(workoutExercise.getOrderIndex())
                .notes(workoutExercise.getNotes())
                .exerciseId(workoutExercise.getExercise().getId())
                .exerciseName(workoutExercise.getExercise().getName())
                .muscleGroup(workoutExercise.getExercise().getMuscleGroup())
                .sets(sets)
                .totalSets(workoutExercise.getTotalSets())
                .completedSets((int) workoutExercise.getCompletedSets())
                .totalVolume(workoutExercise.getTotalVolume())
                .build();
    }


    /**
     * Convierte Workout a WorkoutResponseDTO
     */
    public WorkoutResponseDTO toWorkoutResponseDTO(Workout workout) {
        List<WorkoutExerciseResponseDTO> exercises = workout.getWorkoutExercises().stream()
                .map(this::toWorkoutExerciseResponseDTO)
                .toList();

        // Calcular estadísticas totales
        int totalExercises = exercises.size();
        int totalSets = exercises.stream()
                .mapToInt(WorkoutExerciseResponseDTO::getTotalSets)
                .sum();
        double totalVolume = exercises.stream()
                .mapToDouble(e -> e.getTotalVolume() != null ? e.getTotalVolume() : 0.0)
                .sum();

        return WorkoutResponseDTO.builder()
                .id(workout.getId())
                .name(workout.getName())
                .date(workout.getDate())
                .durationMinutes(workout.getDurationMinutes())
                .notes(workout.getNotes())
                .completed(workout.getCompleted())
                .createdAt(workout.getCreatedAt())
                .userId(workout.getUser().getId())
                .username(workout.getUser().getUsername())
                .exercises(exercises)
                .totalExercises(totalExercises)
                .totalSets(totalSets)
                .totalVolume(totalVolume)
                .build();
    }

    /**
     * Convierte Workout a WorkoutResponseDTO sin ejercicios (para listas)
     */
    public WorkoutResponseDTO toWorkoutResponseDTOSimple(Workout workout) {
        return WorkoutResponseDTO.builder()
                .id(workout.getId())
                .name(workout.getName())
                .date(workout.getDate())
                .durationMinutes(workout.getDurationMinutes())
                .notes(workout.getNotes())
                .completed(workout.getCompleted())
                .createdAt(workout.getCreatedAt())
                .userId(workout.getUser().getId())
                .username(workout.getUser().getUsername())
                .totalExercises(workout.getWorkoutExercises().size())
                .build();
    }
}
