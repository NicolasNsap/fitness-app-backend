package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * SESION DE ENTRENAMIENTO COMPLETA DEL USUARIO
 *
 * RELACIONES:
 * un user tiene muchos Workouts(entrenamientos) 1->N
 * un Workout tiene muchos WorkoutExercises (1:N)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workouts")
public class Workout {

    private UUID id;

    /**
     * usuario duenio de este workout
     * relación: muchos workouts pertenecen a este usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * nombre del workout(entrenamiento)
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * fecha del entrenamiento
     * se usa LocalDate porque solo importa el dia, no la hora exacta
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * duracion total del workout(entrenamiento) en minutos
     * se calcula al finalizar o se ingresa manualmente
     */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * notas opcionales del workout(entrenamiento
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Indica si el workout(entrenamiento) fue completado
     * util para distinguir entre workouts en progreso vs terminados
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * fecha y hora de creacion del registro
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * lista de ejercicios en este workout(entrenamiento)
     *
     * CascadeType.ALL: Las operaciones en Workout se propagan a WorkoutExercise
     * orphanRemoval: Si se elimina un ejercicio de la lista, se borra de la BD
     */
    @Builder.Default
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();


    // MÉTODOS HELPER
    /**
     * Agrega un ejercicio al workout
     * Mantiene la relación bidireccional sincronizada
     */
    public void addWorkoutExercise(WorkoutExercise workoutExercise) {
        workoutExercises.add(workoutExercise);
        workoutExercise.setWorkout(this);
    }

    /**
     * Elimina un ejercicio del workout
     */
    public void removeWorkoutExercise(WorkoutExercise workoutExercise) {
        workoutExercises.remove(workoutExercise);
        workoutExercise.setWorkout(null);
    }

}
