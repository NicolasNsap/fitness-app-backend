package com.fitnessapp.service;

import com.fitnessapp.dto.request.CreateSetRequestDTO;
import com.fitnessapp.dto.request.CreateWorkoutExerciseRequestDTO;
import com.fitnessapp.dto.request.CreateWorkoutRequestDTO;
import com.fitnessapp.dto.request.UpdateWorkoutRequestDTO;
import com.fitnessapp.dto.response.WorkoutExerciseResponseDTO;
import com.fitnessapp.dto.response.WorkoutResponseDTO;
import com.fitnessapp.entity.*;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.mapper.WorkoutMapper;
import com.fitnessapp.repository.ExerciseRepository;
import com.fitnessapp.repository.UserRepository;
import com.fitnessapp.repository.WorkoutRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * logica de negocio
 *
 * maneja todas las operaciones relacionadas con los workouts:
 * -crear - leer - actiualizar - eliminar workouts
 * -calcular estadisticas
 */
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutMapper workoutMapper;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, ExerciseRepository exerciseRepository, WorkoutMapper workoutMapper){
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutMapper = workoutMapper;
    }

    /**
     * CREAR WORKOUT(ENTRENAMIENTO)
     *
     * crear un nuevo entrenamiento completo con (ejercicios y series(sets))
     *
     * @param userId ID del usuario que crear el ejercicio
     * @param request datos del workouts
     * @return WorkoutResponseDTO con el workout creado
     */
    @Transactional
    public WorkoutResponseDTO createWorkout(UUID userId, CreateWorkoutRequestDTO request){
        //buscar el usuario
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //crear en workout(entrenamiento)
        Workout workout = Workout.builder()
                .user(user)//el usuario encontrado
                .name(request.getName())//nombre del DTO ej:push day
                .date(request.getDate())//fecha del DTO
                .durationMinutes(request.getDurationMinutes())//duracion en minutos, puede ser null
                .notes(request.getNotes())//notas opcionales
                .build();//fianliza el builder y crea el objeto

        //agregar ejercicios si vienen en el request
        //la lista existe?, Y la lista tiene elementos?
        if(request.getExercises() != null && !request.getExercises().isEmpty()){
            //recorrer mediante un for para obtener cada ejercicio
            for (CreateWorkoutExerciseRequestDTO exerciseDTO : request.getExercises()){
                //conversion del DTO a una entidad workoutExercise
                WorkoutExercise workoutExercise = createWorkoutExercise(exerciseDTO);
                //agreda el ejercicio a la lista y establece relacion bidireccional
                workout.addWorkoutExercise(workoutExercise);
            }
        }
        //guardar u retornar
        //se guarda en al BD
        Workout savedWorkout = workoutRepository.save(workout);
        //conversion a DTO de la entidad guardada
        return workoutMapper.toWorkoutResponseDTO(savedWorkout);
    }

    /**
     * crear un workoutexercise a partir del DTO
     * @param exerciseDTO
     * @return
     */
    private WorkoutExercise createWorkoutExercise(CreateWorkoutExerciseRequestDTO exerciseDTO) {
        //buscar ejercicio del catalogo
        Exercise exercise = exerciseRepository.findById(exerciseDTO.getExerciseId()).orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + exerciseDTO.getExerciseId()));

        //crear el workout exercise
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .exercise(exercise)//referencia al ejercicio del catalogo
                .orderIndex(exerciseDTO.getOrderIndex())//orden de los ejercicios
                .notes(exerciseDTO.getNotes())//notas
                .build();

        //si hay sets se agregan uno por uno
        if(exerciseDTO.getSets() != null && !exerciseDTO.getSets().isEmpty()){
            for(CreateSetRequestDTO setDTO : exerciseDTO.getSets()){
                ExerciseSet set = createExerciseSet(setDTO);
                workoutExercise.addSet(set);
            }
        }
        return workoutExercise;//retorna el ejercicio completo con sus sets
    }

    /**
     * crear un ExerciseSet(serie del ejercicio) a partir del DTO
     * @param setDTO
     * @return
     */
    private ExerciseSet createExerciseSet(CreateSetRequestDTO setDTO) {
        return ExerciseSet.builder()
                .setNumber(setDTO.getSetNumber())//numero de set(serie)
                .setType(setDTO.getSetType() != null ? setDTO.getSetType() : "WORKING")//tipo de serie
                .weight(setDTO.getWeight())//peso utilizado
                .reps(setDTO.getReps())//repeticiones conseguidas
                .rir(setDTO.getRir())//repeticiones en reserva
                .rpe(setDTO.getRpe())
                .restSeconds(setDTO.getRestSeconds())
                .notes(setDTO.getNotes())
                .completed(false)
                .build();

    }

    /**
     * OBTENER WORKOUTS (Entrenamientos)
     */

    /**
     * obtener un workout(entrenamiento) por su id
     */
    @Transactional(readOnly = true)
    public WorkoutResponseDTO getWorkoutById(UUID workoutId, UUID userId){
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("entrenamiento no encontrado"));

        //verificar que el entrenamiento pertenece al usuario
        if(!workout.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("entrenamiento no encontrado");
        }
        //convierte a DTO y retorna
        return workoutMapper.toWorkoutResponseDTO(workout);
    }

    /**
     * Obtiene todos los workouts(entrenamientos) de un usuario
     */
    public List<WorkoutResponseDTO> getUserWorkouts(UUID userId){
        List<Workout> workouts = workoutRepository.findByUserIdOrderByDateDesc(userId);//busca todos los entrenamientos del usuario ordenados por fecha descendente
        //se convierte la lista a un straam para transformarla
        return workouts.stream()
                .map(workoutMapper :: toWorkoutResponseDTOSimple)//se toma cada entrenamiento y se transforma a responseDTO uno por uno
                .toList();//se convierte el stream denuevo en una lista con los entrenaiento convertidos a DTO
    }

    /**
     * Obtiene workouts de un usuario en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<WorkoutResponseDTO> getUserWorkoutsByDateRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Workout> workouts = workoutRepository
                .findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
        return workouts.stream()
                .map(workoutMapper::toWorkoutResponseDTOSimple)
                .toList();
    }

    /**
     * Obtiene workouts de un usuario en una fecha específica
     */
    @Transactional(readOnly = true)
    public List<WorkoutResponseDTO> getUserWorkoutsByDate(UUID userId, LocalDate date) {
        List<Workout> workouts = workoutRepository.findByUserIdAndDate(userId, date);
        return workouts.stream()
                .map(workoutMapper::toWorkoutResponseDTO)
                .toList();
    }

    /**
     * ACTUALIZAR WORKOUT (ENTRENAMIENTO)
     */

    /**
     * Actualizar un workout(entrenamiento) existente
     */
    @Transactional
    public WorkoutResponseDTO updateWorkout(
            UUID workoutId,
            UUID userId,
            UpdateWorkoutRequestDTO request
    ) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout no encontrado"));

        // Verificar que el workout pertenece al usuario
        if (!workout.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout no encontrado");
        }

        // Actualizar solo los campos que vienen en el request
        if (request.getName() != null) {
            workout.setName(request.getName());
        }
        if (request.getDate() != null) {
            workout.setDate(request.getDate());
        }
        if (request.getDurationMinutes() != null) {
            workout.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getNotes() != null) {
            workout.setNotes(request.getNotes());
        }
        if (request.getCompleted() != null) {
            workout.setCompleted(request.getCompleted());
        }

        Workout updatedWorkout = workoutRepository.save(workout);
        return workoutMapper.toWorkoutResponseDTO(updatedWorkout);
    }

    /**
     * Marca un workout como completado
     */
    @Transactional
    public WorkoutResponseDTO completeWorkout(UUID workoutId, UUID userId, Integer durationMinutes) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout no encontrado"));

        // Verificar que el workout pertenece al usuario
        if (!workout.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout no encontrado");
        }

        workout.setCompleted(true);
        if (durationMinutes != null) {
            workout.setDurationMinutes(durationMinutes);
        }

        // Marcar todos los sets como completados
        workout.getWorkoutExercises().forEach(we ->
                we.getSets().forEach(set -> set.setCompleted(true))
        );

        Workout updatedWorkout = workoutRepository.save(workout);
        return workoutMapper.toWorkoutResponseDTO(updatedWorkout);
    }

    /**
     * ELIMINAR UN WORKOUT(ENTRENAIENTO)
     */

    /**
     * Elimina un workout
     */
    @Transactional
    public void deleteWorkout(UUID workoutId, UUID userId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout no encontrado"));

        // Verificar que el workout pertenece al usuario
        if (!workout.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout no encontrado");
        }

        workoutRepository.delete(workout);
    }

    /**
     * AGREGAR EJERCICIO A WORKOUT EXISTENTE
     */

    /**
     * Agrega un ejercicio a un workout existente
     */
    @Transactional
    public WorkoutResponseDTO addExerciseToWorkout(
            UUID workoutId,
            UUID userId,
            CreateWorkoutExerciseRequestDTO request
    ) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout no encontrado"));

        // Verificar que el workout pertenece al usuario
        if (!workout.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Workout no encontrado");
        }

        // Crear y agregar el ejercicio
        WorkoutExercise workoutExercise = createWorkoutExercise(request);
        workout.addWorkoutExercise(workoutExercise);

        Workout updatedWorkout = workoutRepository.save(workout);
        return workoutMapper.toWorkoutResponseDTO(updatedWorkout);
    }

    /**
     * ESTADÍSTICAS
     */

    /**
     * Cuenta el total de workouts de un usuario
     */
    public long countUserWorkouts(UUID userId) {
        return workoutRepository.countByUserId(userId);
    }

    /**
     * Cuenta los workouts completados de un usuario
     */
    public long countCompletedWorkouts(UUID userId) {
        return workoutRepository.countByUserIdAndCompletedTrue(userId);
    }

}
