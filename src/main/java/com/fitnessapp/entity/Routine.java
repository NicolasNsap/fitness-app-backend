package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     *Usuario duenio de este workout
     *relación: muchos workouts pertenecen a este usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     *nombre del workout(entrenamiento)
     */
    @Column(nullable = false, length = 100)
    private String name;


    /**
     *Duración total del workout(entrenamiento) en minutos
     * se calcula al finalizar o se ingresa manualmente
     */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     *Notas opcionales del workout(entrenamiento
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Fecha y hora de creación del registro
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Lista de ejercicios en este workout(entrenamiento)
     *
     * CascadeType.ALL: Las operaciones en Workout se propagan a WorkoutExercise
     * orphanRemoval: Si se elimina un ejercicio de la lista, se borra de la BD
     */
    @Builder.Default
    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineExercise> routineExercises = new ArrayList<>();

    /**
     * Agrega un ejercicio a la routine
     * Mantiene la relación bidireccional sincronizada
     */
    public void addRoutineExercise(RoutineExercise routineExercise) {
        routineExercises.add(routineExercise);
        routineExercise.setRoutine(this);
    }

    /**
     * Elimina un ejercicio del workout
     */
    public void removeRoutineExercise(RoutineExercise routineExercise) {
        routineExercises.remove(routineExercise);
        routineExercise.setRoutine(null);
    }

    /**
     * Fecha y hora de actualización del registro
     */
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

}
