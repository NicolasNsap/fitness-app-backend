package com.fitnessapp.repository;


import com.fitnessapp.entity.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, UUID> {
    List<RoutineExercise> findByRoutineIdOrderByOrderIndexAsc(UUID routineId);
}
