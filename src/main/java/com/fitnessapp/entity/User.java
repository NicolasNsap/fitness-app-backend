package com.fitnessapp.entity;

import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class User {
    //CAMPOS
    private UUID id;
    private String email;
    private String passwordHash;
    private Integer heightCm;
    private Double weightKg;
    private Double bodyFatPct;
    private Set<Role> roles = new HashSet<>();
    private LocalDateTime createdAt;
}
