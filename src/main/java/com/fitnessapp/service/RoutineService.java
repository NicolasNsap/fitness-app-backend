package com.fitnessapp.service;

import com.fitnessapp.dto.request.CreateRoutineExerciseRequestDTO;
import com.fitnessapp.dto.request.CreateRoutineRequestDTO;
import com.fitnessapp.dto.request.CreateSetRequestDTO;
import com.fitnessapp.dto.request.CreateWorkoutExerciseRequestDTO;
import com.fitnessapp.dto.response.RoutineResponseDTO;
import com.fitnessapp.entity.*;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.mapper.RoutineMapper;
import com.fitnessapp.repository.ExerciseRepository;
import com.fitnessapp.repository.RoutineRepository;
import com.fitnessapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineMapper routineMapper;

    /**
     * CREAR ROUTINE
     * crear una nueva rutina completa
     *
     * @param userId
     * @param requestDTO
     * @return
     */
    @Transactional
    public RoutineResponseDTO createRoutine(UUID userId, CreateRoutineRequestDTO requestDTO){
        //validaciones
        //buscar el usuario
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //crear la rutina
        Routine routine  = Routine.builder()
                .user(user)
                .name(requestDTO.getName())
                .durationMinutes(requestDTO.getDurationMinutes())
                .notes(requestDTO.getNotes())
                .build();

        //agregar ejercicios si vienen en el request
        //la lista existe?, la lista tiene elementos?
        if(requestDTO.getRoutineExercises() != null && !requestDTO.getRoutineExercises().isEmpty()){
            //recorrer mediante un for para obtener cada ejercicio
            for(CreateRoutineExerciseRequestDTO exerciseDTO : requestDTO.getRoutineExercises()){
                //convertir el DTO a una entidad routineExercise
                RoutineExercise routineExercise = createRoutineExercise(exerciseDTO);
                //agregar el ejercicio a la lista y establecer relacion bidireccional
                routine.addRoutineExercise(routineExercise);
            }
        }
        //guardar y retornar
        //se guarda en la BD
        Routine savedRoutine = routineRepository.save(routine);
        //conversion a DTO de la entidad guardada
        return routineMapper.toRoutineResponseDTO(savedRoutine);
    }

    /**
     * Crear un routineExercise a partir del DTO
     * @param exerciseDTO
     * @return
     */
    @Transactional
    private RoutineExercise createRoutineExercise(CreateRoutineExerciseRequestDTO exerciseDTO) {
        Exercise exercise = exerciseRepository.findById(exerciseDTO.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        return RoutineExercise.builder()
                .exercise(exercise)
                .orderIndex(exerciseDTO.getOrderIndex())
                .sets(exerciseDTO.getSets())
                .targetReps(exerciseDTO.getTargetReps())
                .restSeconds(exerciseDTO.getRestSeconds())
                .notes(exerciseDTO.getNotes())
                .build();
    }



    /**
     * Se obtienen todas las rurinas de un usuario
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<RoutineResponseDTO> getUserRoutines(UUID userId){
        //se buscan todos los entrenamientos del usuario contenidos en una lista
        List<Routine> routines = routineRepository.findByUserId(userId);
        //se convierte la lista a un stream (cinta)
        return routines.stream()
                .map(routineMapper :: toRoutineResponseDTO)//se aísla cada entrenamiento y se convierte a DTO
                .toList();//se retorna a una lista con los DTO


    }

    /**
     * Obtener un entrenamiento por su id
     * @param routineId
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public RoutineResponseDTO getRoutineById(UUID routineId, UUID userId){
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));

        //verificar que la rutina pertenece al usuario
        if(!routine.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("rutina no encontrada");
        }
        //convierte a DTO y retorna
        return routineMapper.toRoutineResponseDTO(routine);
    }

    /**
     * Eliminar una rutina
     * @param routineId
     * @param userId
     */
    @Transactional
    public void deleteRoutine(UUID routineId, UUID userId){
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));

        //verificar que la rutina pertenece al usuario
        if(!routine.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("Rutina no encontrada");
        }
        routineRepository.delete(routine);

    }

}
