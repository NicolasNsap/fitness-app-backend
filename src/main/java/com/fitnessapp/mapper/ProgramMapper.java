package com.fitnessapp.mapper;

import com.fitnessapp.dto.response.ProgramResponseDTO;
import com.fitnessapp.dto.response.ProgramRoutineResponseDTO;
import com.fitnessapp.dto.response.RoutineResponseDTO;
import com.fitnessapp.entity.Program;
import com.fitnessapp.entity.ProgramRoutine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramMapper {

    private final RoutineMapper routineMapper;

    public ProgramResponseDTO toProgramResponseDTO(Program program){
        return ProgramResponseDTO.builder()
                .id(program.getId())
                .name(program.getName())
                .startDate(program.getStartDate())
                .durationWeeks(program.getDurationWeeks())
                .goal(program.getGoal())
                .progressionType(program.getProgressionType())
                .active(program.getActive())
                .programRoutines(program.getProgramRoutines().stream().map(this::toProgramRoutineResponseDTO).toList())
                .build();

    }

    public ProgramRoutineResponseDTO toProgramRoutineResponseDTO(ProgramRoutine programRoutine){
        return ProgramRoutineResponseDTO.builder()
                .id(programRoutine.getId())
                .dayNumber(programRoutine.getDayNumber())
                .routine(routineMapper.toRoutineResponseDTO(programRoutine.getRoutine()))
                .build();
    }


}
