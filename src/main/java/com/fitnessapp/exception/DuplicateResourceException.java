package com.fitnessapp.exception;

import lombok.Getter;

/**
 * Exepcion base para todas las excepciones del negocio
 *
 * -Permite capturar todas las excepciones del negocio en un solo handler
 * -Facilita agregar campos comunes en todas las excepciones
 * -Mejor organizacion y jerarquia de excepciones
 *
 * Es la clase padre de todas las excepciones de la aplicaion
 */
@Getter
public  class DuplicateResourceException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;


    /**
     * Constructor principal
     * ejemplo de uso:
     * throw new DuplicateResourceException("Usuario", "id", 999);
     *
     * genera el mensaje: "usuario no encontrado con ID: id"
     *
     * @param resourceName nombre del recurso
     * @param fieldName nombre del campo
     * @param fieldValue valor buscado
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object  fieldValue){
        super(String.format("Ya existe un %$ con %$: %$", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }


    /**
     * Constructor con mensaje personalizado
     * Ejemplo de uso:
     * throw new ResourceNotFoundException("No se encontró el usuario con ese token");
     *
     * @param message mensaje personalizado
     */
    public DuplicateResourceException(String message){
        super(message);
        this.resourceName = null;
        this.fieldName  = null;
        this.fieldValue = null;
    }


}
