package com.fitnessapp.exception.handler;

import com.fitnessapp.dto.response.ErrorResponse;
import com.fitnessapp.exception.DuplicateResourceException;
import com.fitnessapp.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)//este metodo se lanza cuando @Valid detecta que le objeto no cumple las validaciones del UserRequestDTO
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        //MethodArgumentNotValidException internamente tiene exception.getBindingResult() res de la validacion y .getAllErrors() lista errores encontrados
        log.warn("Validation error: {}", exception.getMessage());
        //log.warn registra el error en los logs con nivel warn(advertencia) se usar warn ya que es un error del ciente no es critico si fuera del sevidor seria log.error

        // Extraer errores de validación
        Map<String, Object> validationErrors = new HashMap<>();//se crea un HashMap para guardar los errores en formato clave:valor
        exception.getBindingResult().getAllErrors().forEach(error -> {//exception.getBindingResult().getAllErrors() obtiene todos los errores
            //BindingResult contiene los resultados de la validacion, campos que fallaron y porque
            //getAllErrors() retorna una lista de errores
            //forEach itera los erroes
            String fieldName = ((FieldError) error).getField();//aca con ((FieldError) error) se realiza un downcasting(conversion de tipos) para obterner el metodo getField()
            //fieldName extrae nombre del campo
            //((FieldError) error).getField() funcina porque en @Valid los errores siempre son FieldError(campos especificos) no GlobalError
            String errorMessage = error.getDefaultMessage();
            //errorMessage extrase mensaje de error
            //getDefaultMessage retorna el mensaje obtenido en la anotacion de validacion @Valid
            validationErrors.put(fieldName, errorMessage);
            //el resultado obtenido se guarda en el HashMap creado
        });

        //construccion de la respuesta del error, lo que el cliente recibe si al momento de realizar la peticion comete algun error
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())  //codigo de error en este caso 400
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())//mala recepcion "bad request"
                .message("Error de validación en los datos enviados")
                .path(request.getRequestURI())//url en este caso "/api/users/register"
                .details(validationErrors)//detalles del error en la respuesta
                .build();//objeto contruido

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
