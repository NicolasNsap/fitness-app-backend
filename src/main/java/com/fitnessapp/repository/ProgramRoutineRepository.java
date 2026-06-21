package com.fitnessapp.repository;

import com.fitnessapp.entity.Program;
import com.fitnessapp.entity.ProgramRoutine;
import com.fitnessapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgramRoutineRepository extends JpaRepository<ProgramRoutine, UUID> {

    List<ProgramRoutine> findByProgramIdOrderByDayNumberAsc(UUID programId);
}
