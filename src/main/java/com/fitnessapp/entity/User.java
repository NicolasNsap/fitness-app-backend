package com.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "users")
@Entity//esto índica a Jpa uqe request será una tabla en la base de datos
@Data//annotation de lombok ayuda a la creación de constructores getters y setters
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    //CAMPOS
    @Id//clave primaria
    @GeneratedValue(strategy = GenerationType.UUID)//genera una UUID automáticamente
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)//no puede ser nulo ni repetido
    private String email;

    //contraseña encriptada nuca se guarda en texto plano
    @Column(nullable = false)//no puede ser nulo
    private String passwordHash;

//    private Integer heightCm;//ser usara Integer no int para permitir null
//    private Double weightKg;
//    private Double bodyFatPct;


    //fecha de creación del usuario
    @CreationTimestamp//hibérnate establece automáticamente al crear
    private LocalDateTime createdAt;

    //fecha de última actualización
    @UpdateTimestamp//Se actualiza automáticamente en cada cambio
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne//request es el dueño de esta relación
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")//@JoinColumn especifica la columna FK en la tabla users
    private Role role;

    //indicador de sí la cuenta está activa
    @Builder.Default
    @Column(name = "is_active")
    private boolean isActive = true;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
