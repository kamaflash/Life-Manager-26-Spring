package com.pet.businessdomain.personservice.repository;

import com.pet.businessdomain.personservice.entities.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {
    // Aquí podrías añadir consultas personalizadas si necesitas

    // Spring Data JPA generará la consulta automáticamente
    Optional<CharacterEntity> findByUid(Long uid);
}

