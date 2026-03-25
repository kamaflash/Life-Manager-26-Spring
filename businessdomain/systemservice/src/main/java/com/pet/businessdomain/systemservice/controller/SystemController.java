/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.systemservice.controller;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.SystemDto;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.exceptions.BusinessRuleException;

import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import com.pet.businessdomain.systemservice.services.SystemService;

/**
 *
 * @author Pc
 */
@Slf4j
@RestController
@RequestMapping("/api/systems")
public class SystemController {
    private static final int SIZE = 5;

    @Autowired
    private SystemService systemService;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private SystemMapper systemMapper;

    @Autowired
    private BusinessTransactions businessTransactions;

    @GetMapping
    public ResponseEntity<?> getAllSystems(
            @RequestParam(name = "page",defaultValue = "0") int page) {
        int size = 0;
        if(page == 0) {
            size=9;
        } else {
            size = SIZE;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<SystemEntity> systemsPage = systemRepository.findAll(pageable);

        if (systemsPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No existen mascotas");
        }
        List<SystemEntity> petsList = systemsPage.getContent();
        List<SystemDto> listPetDto = systemMapper.toDtoList(petsList);
        Map<String, Object> response = new HashMap<>();
        response.put("systems", listPetDto);
        response.put("currentPage", systemsPage.getNumber());
        response.put("totalItems", systemsPage.getTotalElements());
        response.put("totalPages", systemsPage.getTotalPages());
        response.put("pageSize", systemsPage.getSize());
        response.put("hasNext", systemsPage.hasNext());
        response.put("hasPrevious", systemsPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/uid/{uid}")
    public SystemDto getSystemByUID(@PathVariable(name="uid") Long uid) throws BusinessRuleException {
        Optional<SystemEntity> optSystem = systemService.getSystemByUid(uid);
        SystemEntity system = systemMapper.fromOptional(optSystem);
        SystemDto systemDto = systemMapper.toDto(system);
        log.info("systemDto: "+systemDto);

            return systemDto;

    }

    @PostMapping
    public ResponseEntity<?> createSystem(@RequestBody SystemDto systemDto) throws BusinessRuleException, UnknownHostException, MessagingException {
        // Convertir DTO a Entidad
        systemDto = systemService.createSystem(systemDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(systemDto);
    }
    @PostMapping("/firts_advance")
    public LocalTime firtsAdvance(@RequestParam(name = "characterId") Long characterId) throws BusinessRuleException, UnknownHostException, MessagingException {
        Optional<SystemEntity> optSystem = systemService.getSystemById(characterId);
        SystemEntity system = systemMapper.fromOptional(optSystem);
        SystemDto systemDto = systemMapper.toDto(system);

        CharacterDto characterDto = businessTransactions.getPerson(systemDto.getUid());
        LocalTime time = systemService.getEducationEndTime(characterDto);
        log.info("systemDto: "+systemDto);

        return time;
    }

    @PostMapping("/post")
    public SystemDto createSystemPost(@RequestBody SystemDto systemDto) throws BusinessRuleException, UnknownHostException, MessagingException {
        systemDto = systemService.createSystem(systemDto);

        return systemDto;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSystem(@PathVariable(name="id") Long id, @RequestBody SystemDto systemDto) throws BusinessRuleException {
        if (systemDto != null) {
            SystemDto dto = systemService.updateSystem(id, systemDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No es aceptable");
        }
    }



    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        systemRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hecho");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSystem(@PathVariable(name = "id") Long id) {
        Optional<SystemEntity> find = systemRepository.findById(id);
        if (find.isPresent()) {
            SystemDto systemDto = systemMapper.toDto(find.get());
            systemRepository.delete(find.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(systemDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No es aceptable");
        }
    }

}
