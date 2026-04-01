package com.fitnessapp.repository;

import com.fitnessapp.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * acceso a datos para la entidad Workout
 * Spring Data JPA genera las implementciones automaticamente
 */
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    /**
     * busca todos los workouts(entrenamientos de un usuaario)
     * SQL: SELECT * FROM workouts WHERE user_id = ? ORDER BY date DESC
     */
    List<Workout> findByUserIdOrderByDateDesc(UUID userId);

    /**
     * busca workouts de un usuario en una fecha especifica
     * SQL: SELECT * FROM workouts WHERE user_id = ? AND date = ?
     */
    List<Workout> findByUserIdAndDate(UUID userId, LocalDate date);

    /**
     * busca workout de usuarios en un rango de fechas
     * util para ver historial semanal/mensual
     */
    List<Workout> findByUserIdAndDateBetweenOrderByDateDesc(UUID userId, LocalDate startDate, LocalDate endDate);

    /**
     * busca workouts completados de un usuario
     */
    List<Workout> findByUserIdAndCompletedTrueOrderByDateDesc(UUID userId);

    /**
     * Busca workouts por nombre (contiene texto, ignora mayúsculas)
     * Útil para buscar "Push", "Leg", etc.
     */
    List<Workout> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);

    /**
     * Cuenta el número de workouts de un usuario
     */
    long countByUserId(UUID userId);

    /**
     * Cuenta workouts completados de un usuario
     */
    long countByUserIdAndCompletedTrue(UUID userId);
}
