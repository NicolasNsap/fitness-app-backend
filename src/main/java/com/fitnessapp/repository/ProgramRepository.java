package com.fitnessapp.repository;

import com.fitnessapp.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {

    /**
     * Para encontrar todos los programas del usuario
     * @param userId
     * @return
     */
    List<Program> findByUserId(UUID userId);

    /**
     * Para encontrar los programas o mesociclos activos del usuario
     * @param userId
     * @return
     */
    Optional<Program> findByUserIdAndActiveTrue(UUID userId);
}
