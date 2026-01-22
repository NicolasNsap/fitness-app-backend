package com.fitnessapp.exception;

import lombok.Getter;

/**
 * Se lanza cuando un recurso no existe
 * Codigo HTTP 404 not found
 *
 * casos de uso:
 * -usuario no encontrado por ID
 * -ejercicio no existe en el catálogo
 * -rutina no encontrada
 *
 * Es como buscar un libro en una biblioteca y no encontrarlo
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Nombre del recurso no encontrado
     */
    private final String resourceName;

    /**
     * Nombre del campo usado para buscar
     */
    private final String fieldName;

    /**
     * Valor del campo buscado
     */
    private final Object fieldValue;//esta variable es de tipo Object porque puede ser de diferentes tipos ej: un nombre, un id, un email, un boolean

    /**
     * Constructor principal
     * ejemplo de uso:
     * throw new ResourceNotFoundException("Usuario", "id", 999);
     *
     * genera el mensaje: "usuario no encontrado con ID: id"
     *
     * @param resourceName nombre del recurso
     * @param fieldName nombre del campo
     * @param fieldValue valor buscado
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue){
        super(String.format("%s no encontrado con %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructor simplificado solo con él, id
     * este constructor se crea, ya que la mayoría de las veces se busca por ID
     *
     * ejemplo de uso:
     * throw new ResourceNotFoundException("Usuario", 999L);
     * Genera mensaje: "Usuario no encontrado con ID: 999"
     *
     * @param resourceName nombre del recurso
     * @param id ID del recurso
     */
    public ResourceNotFoundException(String resourceName, Long id){
        this(resourceName, "id", id);//en este caso this llama al constructor principal de esta misma clase
    }

    /**
     * Constructor con mensaje personalizado
     * Ejemplo de uso:
     * throw new ResourceNotFoundException("No se encontró el usuario con ese token");
     *
     * @param message mensaje personalizado
     */
    public ResourceNotFoundException(String message){
        super(message);
        this.resourceName = null;
        this.fieldName  = null;
        this.fieldValue = null;
    }
}
