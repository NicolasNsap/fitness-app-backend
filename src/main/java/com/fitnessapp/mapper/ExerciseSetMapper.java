package com.fitnessapp.mapper;

import com.fitnessapp.dto.response.SetResponseDTO;
import com.fitnessapp.entity.ExerciseSet;
import org.springframework.stereotype.Component;

@Component
public class ExerciseSetMapper {


    public SetResponseDTO toSetResponseDTO(ExerciseSet updateSet) {
        return SetResponseDTO.builder()
                .id(updateSet.getId())
                .weight(updateSet.getWeight())
                .reps(updateSet.getReps())
                .build();


    }
}
