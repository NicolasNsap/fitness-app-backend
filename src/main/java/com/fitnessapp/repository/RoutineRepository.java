package com.fitnessapp.repository;

import com.fitnessapp.entity.Routine;
import com.fitnessapp.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, UUID> {
    /**
     * Rutinas de un usuario
     * @param userId
     * @return
     */
    List<Routine> findByUserId(UUID userId);


}
