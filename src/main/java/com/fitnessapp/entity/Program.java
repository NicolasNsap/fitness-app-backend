package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "programs")
public class Program {

    @Id//clave primaria
    @GeneratedValue(strategy = GenerationType.UUID)//genera una UUID automáticamente
    private UUID id;

    /**
     * Usuario duenio de este program
     * relación: muchos program pertenecen a este usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * nombre del program(programa o mesociclo)
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Fecha de inicio del programa
     */
    private LocalDate startDate;

    private Integer durationWeeks;

    private String goal;

    @Enumerated(EnumType.STRING)//para que se guarde como texto en la BD
    private ProgressionType progressionType;

    @Builder.Default
    private Boolean active = false;

    @Builder.Default
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramRoutine> programRoutines =  new ArrayList<>();

    /**
     * Fecha y hora de la creación del programa
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de actualización del registro
     */
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
