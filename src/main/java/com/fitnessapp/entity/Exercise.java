package com.fitnessapp.entity;

import org.hibernate.validator.constraints.UUID;

public class Exercise {
    //CAMPOS
    private UUID id;
    private String name;
    private String muscleGroup;
    private String videoUrl;
    private boolean active = true;
}
