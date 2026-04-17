package com.pet.businessdomain.eventservice.repository;

import com.pet.businessdomain.eventservice.entities.ObjectiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends JpaRepository<ObjectiveEntity, Long> {
}