package com.fitnessapp.entity;

import org.hibernate.validator.constraints.UUID;

public class Routine {
    //CAMPOS
    private UUID id;
    private UUID userId;
    private TrainingPlan trainingPlan;
    private String name;
    private Integer dayOrder; // Orden dentro del plan
}
