package com.fitnessapp.exception.handler;

import com.fitnessapp.dto.response.ErrorResponse;
import com.fitnessapp.exception.DuplicateResourceException;
import com.fitnessapp.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
/**
 * Es la combinación de 2 anotaciones @ControllerAdvice + @ResponseBody = @RestControllerAdvice
 * @ControllerAdvice intercepta excepciones de todos los controllers
 * @ResponseBody convierte el retorno a JSON automáticamente, convierte el objeto ErrorResponse a JSON automáticamente
 */
@Slf4j
/**
 * lombok genera automáticamente un logger
 */
public class GlobalExceptionHandler {

    //maneja ResourceNotFoundException → 404
    @ExceptionHandler(ResourceNotFoundException.class)//le dice a spring este método maneja excepciones del tipo ResourceNotFoundException
    //cuando cualquier parte de la app lance ResourceNotFoundException, este método se ejecutará.
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request){

        log.error("ResourceNotFoundException: {}", exception.getMessage());//registra el error en los logs del servidor

        //crear detalles adicionales
        Map<String, Object> details = new HashMap<>();
        details.put("resourceName", exception.getResourceName());
        details.put("fieldName", exception.getFieldName());
        details.put("fieldValue", exception.getFieldValue());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())//404
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())//extrae el nombre del status HTTP en este caso "Not Found"
                .message(exception.getMessage())//extrae el mensaje de la excepción
                .path(request.getRequestURI())//extrae la url de la petición que causo el error
                .details(details)//agrega el Map con la información adicional
                .build();//construye el objeto


        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //maneja DuplicateResourceException → 409
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException exception, HttpServletRequest request){

        log.warn("DuplicateResourceException: {}", exception.getMessage());


        Map<String, Object> details = new HashMap<>();
        details.put("resourceName", exception.getResourceName());
        details.put("fieldName", exception.getFieldName());
        details.put("fieldValue", exception.getFieldValue());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())  // 409
                .error(HttpStatus.CONFLICT.getReasonPhrase())  // "Conflict"
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
