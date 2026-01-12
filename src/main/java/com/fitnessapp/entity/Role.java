package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data//Lombok: Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor//lombok: constructor sin parametros requerido por jpa
@AllArgsConstructor//lombok: constructor con todos los parametros requerido por jpa
@Builder//patron builder para crear objetos facilmente
@Entity//le dice a jpa que esta clase es una tabla es una clase en la base BD
@Table(name = "roles")
public class Role {
    //CAMPOS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, length = 20)
    private String name;

    @OneToOne(mappedBy = "role")//no es el duenio de la relacion, dueno de la relaion es request
    private User user;
}
