package com.fitnessapp.repository;

import com.fitnessapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    //busca un rol por su nombre
    Optional<Role> findByName(String name);

    //validar si existe un rol con ese nombre
    boolean existsByName(String name);
}
