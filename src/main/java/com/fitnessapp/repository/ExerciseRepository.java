package com.fitnessapp.repository;

import com.fitnessapp.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    /**
     * Busca un ejercicio por su nombre exacto
     * Genera: SELECT * FROM exercises WHERE name = ?
     */
    Optional<Exercise> findByName(String name);

    /**
     * Verifica si existe un ejercicio con ese nombre
     * Útil para evitar duplicados al crear ejercicios
     */
    boolean existsByName(String name);

    /**
     * Encuentra ejercicios por grupo muscular
     * Genera: SELECT * FROM exercises WHERE muscle_group = ?
     *
     * Ejemplo: findByMuscleGroup("PECHO") → todos los ejercicios de pecho
     */
    List<Exercise> findByMuscleGroup(String muscleGroup);

    /**
     * Encuentra ejercicios por nivel de dificultad
     * Genera: SELECT * FROM exercises WHERE difficulty_level = ?
     *
     * Ejemplo: findByDifficultyLevel("PRINCIPIANTE")
     */
    List<Exercise> findByDifficultyLevel(String difficultyLevel);

    /**
     * Encuentra ejercicios por equipamiento necesario
     * Genera: SELECT * FROM exercises WHERE equipment_needed = ?
     */
    List<Exercise> findByEquipmentNeeded(String equipmentNeeded);

    /**
     * Encuentra solo ejercicios activos
     * Útil para mostrar el catálogo público (sin ejercicios obsoletos)
     */
    List<Exercise> findByIsActiveTrue();

    /**
     * Encuentra ejercicios activos de un grupo muscular específico
     * Combina dos condiciones con AND
     * Genera: SELECT * FROM exercises
     *         WHERE muscle_group =? AND is_active = true
     */
    List<Exercise> findByMuscleGroupAndIsActiveTrue(String muscleGroup);


    /**
     * Encuentra ejercicios por grupo muscular y dificultad
     * Útil para recomendar ejercicios según nivel del usuario
     * Genera: SELECT * FROM exercises
     *         WHERE muscle_group = ? AND difficulty_level = ? AND is_active = true
     */
    List<Exercise> findByMuscleGroupAndDifficultyLevelAndIsActiveTrue(
            String muscleGroup,
            String difficultyLevel
    );

    /**
     * BÚSQUEDA AVANZADA: Encuentra ejercicios por término de búsqueda
     *
     * Busca en nombre, descripción y grupo muscular
     * LIKE permite búsqueda parcial (ej: "press" encuentra "Press de Banca")
     * LOWER() hace la búsqueda case-insensitive (mayús/minús da igual)
     *
     * @param searchTerm Término de búsqueda
     * @return Lista de ejercicios que coincidan
     */
    @Query("SELECT e FROM Exercise e WHERE " +
            "LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.muscleGroup) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Exercise> searchExercises(@Param("searchTerm") String searchTerm);

    /**
     * FILTRADO MÚLTIPLE: Encuentra ejercicios con filtros opcionales
     *
     * Esta query es más compleja porque permite que los parámetros sean null
     * Si son null, no los incluye en el filtro
     *
     * Ejemplo de uso:
     * - muscleGroup="PECHO", difficultyLevel=null, equipmentNeeded=null
     *   → Busca solo por grupo muscular
     * - muscleGroup="PIERNAS", difficultyLevel="AVANZADO", equipmentNeeded="Barra"
     *   → Busca por los 3 criterios
     */
    @Query("SELECT e FROM Exercise e WHERE " +
            "(:muscleGroup IS NULL OR e.muscleGroup = :muscleGroup) AND " +
            "(:difficultyLevel IS NULL OR e.difficultyLevel = :difficultyLevel) AND " +
            "(:equipmentNeeded IS NULL OR e.equipmentNeeded = :equipmentNeeded) AND " +
            "e.isActive = true")
    List<Exercise> findByFilters(
            @Param("muscleGroup") String muscleGroup,
            @Param("difficultyLevel") String difficultyLevel,
            @Param("equipmentNeeded") String equipmentNeeded
    );
}
