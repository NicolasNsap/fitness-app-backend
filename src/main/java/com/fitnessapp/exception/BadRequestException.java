package com.fitnessapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para solicitudes inválidas (400 Bad Request)
 *
 * Se usa cuando:
 * - Contraseña incorrecta
 * - Cuenta desactivada
 * - Datos inválidos que no son de duplicación
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }

}
