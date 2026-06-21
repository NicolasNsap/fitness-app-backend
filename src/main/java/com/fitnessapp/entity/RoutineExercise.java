package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "routine_exercises")
public class RoutineExercise {

    @Id//clave primaria
    @GeneratedValue(strategy = GenerationType.UUID)//genera una UUID automáticamente
    private UUID id;

    /**
     *Routine al que pertenece este ejercicio
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    /**
     * Ejercicio del catálogo que se está realizando
     * referencia a la tabla exercise(catálogo base)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;


    private Integer sets;

    private int targetReps;

    private int restSeconds;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
