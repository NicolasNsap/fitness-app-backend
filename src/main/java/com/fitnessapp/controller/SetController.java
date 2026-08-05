package com.fitnessapp.controller;

import com.fitnessapp.dto.request.UpdateSetRequestDTO;
import com.fitnessapp.dto.response.SetResponseDTO;
import com.fitnessapp.service.SetService;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sets")//Ruta base
@RequiredArgsConstructor
@Slf4j
public class SetController {

    private final SetService setService;
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
     * METODO PARA EDITAR SET
     */
    @PatchMapping("/{setId}")
    public ResponseEntity<SetResponseDTO> updateSet(@PathVariable UUID setId, @Valid @RequestBody UpdateSetRequestDTO requestDTO, Authentication authentication){

        UUID userId = getUserIdFromAuth(authentication);

        SetResponseDTO responseDTO = setService.updateSet(setId, userId, requestDTO);

        return ResponseEntity.ok().body(responseDTO);

    }

    /**
     * METODO PARA ELIMINAR SET
     */
    @DeleteMapping("/{setId}")
    public ResponseEntity<Void> deleteSet(@PathVariable UUID setId, Authentication authentication){
        UUID userId = getUserIdFromAuth(authentication);
        setService.deleteSet(setId, userId);

        return ResponseEntity.noContent().build();
    }




}
