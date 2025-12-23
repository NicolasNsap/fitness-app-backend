package com.fitnessapp.entity;

import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;

public class TrainingPlan {
    //CAMPOS
    private UUID id;
    private User user;
//    private Goal  goal;
    private LocalDate startDate;
    private LocalDate endDate;

    private boolean active;
}
