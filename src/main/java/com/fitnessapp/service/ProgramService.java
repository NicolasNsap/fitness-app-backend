package com.fitnessapp.service;

import com.fitnessapp.dto.request.CreateProgramRequestDTO;
import com.fitnessapp.dto.request.CreateProgramRoutineRequestDTO;
import com.fitnessapp.dto.response.ProgramResponseDTO;
import com.fitnessapp.entity.Program;
import com.fitnessapp.entity.ProgramRoutine;
import com.fitnessapp.entity.Routine;
import com.fitnessapp.entity.User;
import com.fitnessapp.exception.ResourceNotFoundException;
import com.fitnessapp.mapper.ProgramMapper;
import com.fitnessapp.repository.ProgramRepository;
import com.fitnessapp.repository.RoutineRepository;
import com.fitnessapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramService {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final ProgramMapper programMapper;

    /**
     * CREAR UN PROGRAMA - MESOCICLO
     * @param userId
     * @param requestDTO
     * @return
     */
    @Transactional
    public ProgramResponseDTO createProgram(UUID userId, CreateProgramRequestDTO requestDTO){
        //validar que el usuario existe
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //crear un programa
        Program program = Program.builder()
                .user(user)
                .name(requestDTO.getName())
                .startDate(requestDTO.getStartDate())
                .durationWeeks(requestDTO.getDurationWeeks())
                .goal(requestDTO.getGoal())
                .progressionType(requestDTO.getProgressionType())
                .build();

        //agregar rutinas al programa
        if(requestDTO.getProgramRoutines() != null && !requestDTO.getProgramRoutines().isEmpty()){
            for (CreateProgramRoutineRequestDTO routineDTO : requestDTO.getProgramRoutines()){
                ProgramRoutine programRoutine = createProgramRoutine(routineDTO);
                program.addProgramRoutine(programRoutine);
            }
        }
        //guardar y retornar
        Program savedProgram = programRepository.save(program);
        //conversion a DTO
        return programMapper.toProgramResponseDTO(savedProgram);

    }


    /**
     * METODO PARA BUSCAR UNA RUTINA Y ASIGNARLA COMO UNA RUTINA DEL PROGRAMA
     * @param routineDTO
     * @return
     */
    private ProgramRoutine createProgramRoutine(CreateProgramRoutineRequestDTO routineDTO) {
        Routine routine = routineRepository.findById(routineDTO.getRoutineId()).orElseThrow(() -> new ResourceNotFoundException("Rutina no  encontrada"));

        return ProgramRoutine.builder()
                .routine(routine)
                .dayNumber(routineDTO.getDayNumber())
                .build();
    }


    /**
     * OBTENER PROGRAMA DE UN USUARIO
     * @param programId
     * @param userID
     * @return
     */
    @Transactional(readOnly = true)
    public ProgramResponseDTO getProgramById(UUID programId, UUID userID){
        Program program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));

        //verificar que el programa pertenece al usuario
        if(!program.getUser().getId().equals(userID)){
            throw new ResourceNotFoundException("programa no encontrado");
        }

        //convertir a DTO
        return  programMapper.toProgramResponseDTO(program);

    }


    /**
     * MOSTRAR TODOS LOS PROGRAMAS DEL USUARIO
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ProgramResponseDTO> getUserPrograms(UUID userId){
        //se buscan todos los programas del usuario contenidos en una lista
        List<Program> programs = programRepository.findByUserId(userId);

        //se retorna y se realiza el proceso para convertir cada elemento de la lista a responseDTO
        return programs.stream()
                .map(programMapper :: toProgramResponseDTO)
                .toList();//retorno de la lista con sus elementos convertidos a DTO

    }


    /**
     * ELIMINAR UN PROGRAMA
     * @param programId
     * @param userId
     */
    public void deleteProgram(UUID programId, UUID userId){
        Program program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));

        //verifcar que el progrma pertenece al usuario
        if(!program.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("Programa no encontrado");
        }
        programRepository.delete(program);


    }

    //UPDATE PROGRAM QUEDA MAS ADELANTE SE IMPLEMENTARA

    /**
     * DESACTIVAR UN PROGRAMA - MEJOR OPCIÓN A ELIMINAR, YA QUE SE PUEDE VERIFICAR EL HISTORIAL DE PROGAMAS
     * @param programId
     * @param userId
     */
    @Transactional
    public void activateProgram(UUID programId, UUID userId){
        //verificar si existe un programa active
        programRepository.findByUserIdAndActiveTrue(userId)
                .ifPresent(activeProgram -> {
                    activeProgram.setActive(false);
                    programRepository.save(activeProgram);
                });

        Program program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));
        //verifcar que el progrma pertenece al usuario
        if(!program.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("Programa no encontrado");
        }

        program.setActive(true);
        programRepository.save(program);

        log.info("programa activado: {}", program.getName());
    }



}
