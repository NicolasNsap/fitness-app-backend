package com.fitnessapp.service;

import com.fitnessapp.dto.request.ExerciseRequestDTO;
import com.fitnessapp.dto.response.ExerciseResponseDTO;
import com.fitnessapp.entity.Exercise;
import com.fitnessapp.exception.DuplicateResourceException;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.mapper.ExerciseMapper;
import com.fitnessapp.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ExerciseService logica de negocios para ejercicios
 *
 * Responsabilidades:
 * -CRUD de ejercicios (solo ADMIN puede crear/editar/eliminar)
 * -busqueda y filtrado de ejercicios
 * -transformacion DTO <-> Entity
 *
 * Este servicio es más simple que el userService porque no maneja autenticación ni encriptación
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    /**
     * Crear un nuevo ejercicio (solo ADMIN)
     *
     * @param exerciseRequestDTO DTO con datos del ejercicio
     * @return ResponseDTO del ejercicio creado
     * @throws RuntimeException si el nombre ya existe
     */
    @Transactional
    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO){
        log.info("Creando nuevo ejercicio: {}", exerciseRequestDTO.getName());

        //validar que no existe un ejercicio con ese nombre
        //se le setea el nombre al existsByName del repository para verificar si el nombre existe
        if (exerciseRepository.existsByName(exerciseRequestDTO.getName())){
            throw new DuplicateResourceException("Ya existe un ejercicio con el nombre: " + exerciseRequestDTO.getName());
        }

        //convertir exerciseRequestDTO a Entity usando el mapper
        Exercise exercise = exerciseMapper.toEntity(exerciseRequestDTO);

        //guardar en la BD mediante el metodo save del exerciseRepository
        Exercise  savedExercise = exerciseRepository.save(exercise);
        log.info("Ejercicio creado exitosamente con ID: {}", savedExercise.getId());

        //convertir Entity a ResponseDTO usando el método de la clase mapper
        return exerciseMapper.toResponseDTO(savedExercise);
    }


    /**
     * Busca un ejercicio por ID
     *
     * @param id ID del ejercicio
     * @return ResponseDTO del ejercicio
     * @throws RuntimeException si no existe
     */
    @Transactional(readOnly = true) //solo lectura
    public ExerciseResponseDTO findById(UUID id) {
        log.info("Buscando ejercicio con ID: {}", id);

        //si el ID es encontrado en la BD se almacena en la variable exercise sino se lanza una excepción
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio" + id));

        //al método toResponseDTO de la clase exerciseMapper se le pasa la variable exercise que es donde se almaceno el ID
        return exerciseMapper.toResponseDTO(exercise);
    }

    /**
     * Lista todos los ejercicios activos
     *
     * @return Lista de ejercicios activos
     */
    @Transactional(readOnly = true)//solo lectura
    //se usa List<ExerciseResponseDTO>, ya que se retornara una lista de los ejercicios activos
    public List<ExerciseResponseDTO> findAllActiveExercises() {
        log.info("Listando todos los ejercicios activos");

        return exerciseRepository.findByIsActiveTrue()//se llama a los ejercicios activos
                .stream()//la lista se convierte a un stream
                .map(exerciseMapper::toResponseDTO)//cada ejercicio entity se convierte a response DTO para ser mostrado al usuario
                .collect(Collectors.toList());//se recolectan los ejercicios para ser mostrados mediante una lista de ejercicios
    }

    /**
     * Busca ejercicios por grupo muscular
     *
     * @param muscleGroup Grupo muscular (ej.: "PECHO", "PIERNAS")
     * @return Lista de ejercicios del grupo muscular
     */
    @Transactional(readOnly = true)//solo lectura
    public List<ExerciseResponseDTO> findByMuscleGroup(String muscleGroup) {
        log.info("Buscando ejercicios del grupo muscular: {}", muscleGroup);

        return exerciseRepository.findByMuscleGroupAndIsActiveTrue(muscleGroup)
                .stream()
                .map(exerciseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar un ejercicio(solo ADMIN)
     * @param id del ejercicio a actualizar
     * @param exerciseRequestDTO de datos nuevos
     * @return ejercicio actualizado
     * @throws RuntimeException si no existe
     */
    @Transactional
    public ExerciseResponseDTO updateExercise(UUID id, ExerciseRequestDTO exerciseRequestDTO){
        log.info("Actualizando ejercicio con ID: {}", id);

        //se busca el ejercicio en la base de datos
        Exercise exercise = exerciseRepository.findById(id)
                //sino se encuentra se lanza una excepción
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio" + id));

        //se actualizaran los campos usando el método updateEntityFromDTO del  exerciseMapper
        exerciseMapper.updateEntityFromDTO(exercise, exerciseRequestDTO);

        Exercise updatedExercise = exerciseRepository.save(exercise);
        log.info("Ejercicio actualizado exitosamente: {}", updatedExercise.getName());

        return exerciseMapper.toResponseDTO(updatedExercise);
    }

    /**
     * Envez de eliminar un ejercicio se desactivará
     * @param id del ejercicio a desactivar
     */
    @Transactional
    public void desactivateExercise(UUID id){
        log.info("Desactivando ejercicio con ID: {}", id);

        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio" + id));

        exercise.setActive(false);
        exerciseRepository.save(exercise);

        log.info("Ejercicio desactivado: {}", exercise.getName());
    }
}
