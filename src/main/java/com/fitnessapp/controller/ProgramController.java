package com.fitnessapp.controller;

import com.fitnessapp.dto.request.CreateProgramRequestDTO;
import com.fitnessapp.dto.request.CreateRoutineRequestDTO;
import com.fitnessapp.dto.response.ProgramResponseDTO;
import com.fitnessapp.dto.response.RoutineResponseDTO;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.repository.UserRepository;
import com.fitnessapp.service.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
@Slf4j
public class ProgramController {

    private final ProgramService programService;
    private final UserRepository userRepository;

    /**
     * METODO PRIVADO PARA OBTENER EL ID DEL SUSUARIO CON DATOS DEL JWT
     * @param authentication
     * @return
     */
    private UUID getUserIdFromAuth(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"))
                .getId();

    }

    /**
     *METOOD PARA CREAR UN PROGRAMA
     * @param requestDTO
     * @param authentication
     * @return
     */
    @PostMapping
    public ResponseEntity<ProgramResponseDTO> createProgram(@Valid @RequestBody CreateProgramRequestDTO requestDTO, Authentication authentication){
        //se obtiene el ID del método que extrae el id desde el token JWT
        UUID userId = getUserIdFromAuth(authentication);
        //el programa creado se convierte en un responseDTO
        ProgramResponseDTO responseDTO = programService.createProgram(userId, requestDTO);
        //se retorna la respuesta con la rutina creada con el estatus CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }


    /**
     * MOSTRAR TODOS LOS PROGRAMAS DE UN USUARIO
     *
     * @param authentication
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ProgramResponseDTO>> getUserPrograms(Authentication authentication) {
        //se obtiene el ID del método que extrae el id desde el token JWT
        UUID userId = getUserIdFromAuth(authentication);
        //se obtienen los programas y se almacenan en una lista
        List<ProgramResponseDTO> responseDTO = programService.getUserPrograms(userId);
        //se retorna la respuesta con la lista de los programas
        return ResponseEntity.ok().body(responseDTO);
    }


    /**
     * MOSTRAR UN PROGRAMA DE UN USUARIO
     * @param programId
     * @param authentication
     * @return
     */
    @GetMapping("/{programId}")
    public ResponseEntity<ProgramResponseDTO> getProgramById(@PathVariable UUID programId, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        ProgramResponseDTO responseDTO = programService.getProgramById(programId, userId);


        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * ELIMINAR PROGRAMA
     * @param programId
     * @param authentication
     * @return
     */
    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgram(@PathVariable UUID programId, Authentication authentication) {

        UUID userId = getUserIdFromAuth(authentication);

        programService.deleteProgram(programId, userId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{programId}/activate")
    public ResponseEntity<Void> activateProgram(@PathVariable UUID programId, Authentication authentication){
        UUID userId = getUserIdFromAuth(authentication);
        programService.activateProgram(programId, userId);
        return ResponseEntity.noContent().build();

    }

}
