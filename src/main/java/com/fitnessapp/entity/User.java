package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data//annotation de lombox ayuda a la creacion de constructores getters y setters
@NoArgsConstructor
@Entity//esto indica a Jpa uqe user sera una tabla en la base de datos
@Table(name = "users")
public class User {
    //CAMPOS
    @Id//clave primaria
    @GeneratedValue(strategy = GenerationType.UUID)//genera una UUID automaticamente
    private UUID id;

    @Column(nullable = false, unique = true)//no puede ser nulo ni repetido
    private String email;

    @Column(nullable = false)//no puede ser nulo
    private String passwordHash;

    private Integer heightCm;//ser usara Integer no int para permitir null
    private Double weightKg;
    private Double bodyFatPct;

    //si bien  es cierto se puede crear la tabla en la base de datos se hara uso de jpa para crearla
    @ManyToMany(fetch = FetchType.LAZY)//@ManyToMany relacion de muchos a muchos LAZY quiere decir no cargues algo hasta que yo lo pida explicitamente
    @JoinTable(//tabla intermedia
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),//esta columna es la duenia de la relacion osea la controla
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();//se iso uso de un set para evitar duplicados

    @CreationTimestamp//hibernate llena esto automaticamente al crear
    private LocalDateTime createdAt;
}
