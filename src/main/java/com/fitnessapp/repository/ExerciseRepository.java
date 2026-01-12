package com.fitnessapp.repository;

import com.fitnessapp.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    //buscar por nombre exacto
    Optional<Exercise> findByName(String name);

    //verificar si existe un ejercicio con ese nombre
    boolean existsByName(String name);

    //encontrar ejercicios por grupo muscular
    List<Exercise> findByMuscleGroup(String muscleGroup);

    //encuentra solo ejercicios activos
    List<Exercise> findByIsActiveTrue();
}
