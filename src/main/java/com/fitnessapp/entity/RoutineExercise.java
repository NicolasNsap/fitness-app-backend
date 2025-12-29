package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@Entity
@Table(name = "routine_exercises",
        uniqueConstraints = @UniqueConstraint(columnNames = {"routine_id", "exercise_order"}))
public class RoutineExercise {
    //CAMPOS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Integer sets;
    private Integer reps;
    private Integer restSeconds;

    @Column(name = "exercise_order")
    private Integer order;
}
