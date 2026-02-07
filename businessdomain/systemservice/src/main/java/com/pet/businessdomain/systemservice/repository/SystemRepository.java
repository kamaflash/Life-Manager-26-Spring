/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pet.businessdomain.systemservice.repository;

import com.pet.businessdomain.systemservice.entities.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Pc
 */
public interface SystemRepository  extends JpaRepository<SystemEntity, Long> {
    Optional<SystemEntity> findByUid(Long uid);
}
