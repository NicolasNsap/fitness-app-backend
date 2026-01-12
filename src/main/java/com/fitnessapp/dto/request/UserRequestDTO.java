package com.fitnessapp.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
* UserRequestDTO datos necesarios para CREAR el usuario
* campos que no incluye:
* - id(lo genera la BD automaticamente)
* - createdAt / updatedAt (timestamps automáticos)
* - isActive por defecto es true*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    @NotBlank(message = "el username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;


    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    /*
    * el usuario la enviara en formato texto plano, pero se encriptara antes de guardar*/
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    /**
     * NOTA: No incluimos roleId aquí porque:
     * - Por defecto, todo nuevo usuario será "USER"
     * - Solo un ADMIN puede asignar rol "ADMIN"
     * - Lo manejaremos en el Service
     */


}
