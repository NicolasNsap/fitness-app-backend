package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {
    //CAMPOS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String muscleGroup;
    private String videoUrl;
    private boolean active = true;
}
