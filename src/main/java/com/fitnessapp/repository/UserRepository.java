package com.fitnessapp.repository;

import com.fitnessapp.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca un usuario por su username
     * Genera: SELECT * FROM users WHERE username =?
     */
    Optional<User> findByUsername(String username);

    //buscar un usuario por su email
    Optional<User> findByEmail(String email);

    //verificar si existe un usuario con ese nombre
    boolean existsByUsername(String username);

    //verificar si existe un usuario con ese email
    boolean existsByEmail(String email);


}
