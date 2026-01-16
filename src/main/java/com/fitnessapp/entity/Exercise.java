package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {
    //CAMPOS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    //grupo muscular que trabaja
    @Column(name = "muscle_group", nullable = false, length = 50)
    private String muscleGroup;

    //equipamiento necesario
    @Column(name = "equipment_needed", length = 100)
    private String equipmentNeeded;

    //nivel de dificultad del ejercicio
    @Column(name = "difficulty_level", length = 20)
    private String difficultyLevel;

    //url de video demostrativo
    @Column(name = "video_url", length = 255)
    private String videoUrl;

    //indica si el ejercicio está activo en el catálogo
    @Column(name = "is_active")
    private boolean isActive = true;
}
