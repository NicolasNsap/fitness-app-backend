package com.fitnessapp.service;

import com.fitnessapp.dto.request.UpdateSetRequestDTO;
import com.fitnessapp.dto.response.SetResponseDTO;
import com.fitnessapp.entity.ExerciseSet;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.mapper.ExerciseSetMapper;
import com.fitnessapp.repository.ExerciseSetRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service //marca esta clase como un servicio de spring
@RequiredArgsConstructor //inyección por constructor
@Slf4j //logger automático
public class SetService {
    private final ExerciseSetRepository exerciseSetRepository;
    private final ExerciseSetMapper exerciseSetMapper;




    public SetResponseDTO updateSet(UUID setId, UUID userId, @Valid UpdateSetRequestDTO requestDTO) {

        ExerciseSet exerciseSet = exerciseSetRepository.findById(setId).orElseThrow(() -> new ResourceNotFoundException("Set no encontrado"));

        // Verificar que el workout pertenece al usuario
        if (!exerciseSet.getWorkoutExercise().getWorkout().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout no encontrado");
        }

        //actulizar campos
        if (requestDTO.getWeight() != null) {
            exerciseSet.setWeight(requestDTO.getWeight());
        }
        if (requestDTO.getReps() != null) {
            exerciseSet.setReps(requestDTO.getReps());
        }
        if (requestDTO.getCompleted() != null){
            exerciseSet.setCompleted(requestDTO.getCompleted());
        }

        ExerciseSet updateSet = exerciseSetRepository.save(exerciseSet);
        return exerciseSetMapper.toSetResponseDTO(updateSet);
    }

    //eliminar set
    public void deleteSet(UUID setId, UUID userId) {
        ExerciseSet exerciseSet = exerciseSetRepository.findById(setId).orElseThrow(() -> new ResourceNotFoundException("Set no encontrado"));

        // Verificar que el set pertenece al usuario
        if (!exerciseSet.getWorkoutExercise().getWorkout().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("set no encontrado");
        }
        exerciseSetRepository.delete(exerciseSet);
    }
}
