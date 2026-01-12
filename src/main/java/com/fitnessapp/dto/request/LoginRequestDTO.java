package com.fitnessapp.dto.request;
/*
* LoginRequestDTO datso necesarios para hacer LOGIN
* acepta username o email como identificador
* la contrasenia viaja en texto plano (por eso HTTPS es obligatorio*/

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    /*
    * username o email del usuario
    */
    @NotBlank(message = "El username o email es obligatorio")
    private String usernameOrEmail;

    /*
    * contrasenia en texto plano
    * - Siempre usar HTTPS en producción
    * - La contraseña viaja encriptada por HTTPS
    * - El backend la compara con BCrypt
    * - NUNCA se guarda en logs*/
   @NotBlank(message = "La contrasenia es obligatoria")
    private String password;
}
