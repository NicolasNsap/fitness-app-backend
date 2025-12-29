package com.fitnessapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    //CAMPOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
}
