package com.fitnessapp.entity;

import org.hibernate.validator.constraints.UUID;

public class RoutineExercise {
    //CAMPOS
    private UUID id;
    private Routine routine;
    private Exercise exercise;
    private Integer sets;
    private Integer reps;
    private Integer restSeconds;
    private Integer order;
}
