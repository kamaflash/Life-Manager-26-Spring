/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.userservice.services;

import com.pet.businessdomain.shareddto.dto.UserDto;
import com.pet.businessdomain.userservice.entities.User;
import com.pet.businessdomain.userservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.userservice.mapper.UserMapper;
import com.pet.businessdomain.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pet.businessdomain.userservice.transactions.BusinessTransactions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pc
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BusinessTransactions businessTransactions;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String username) {
        return userRepository.findUserByUsernameOrEmail(username, username);
    }

    @Override
    public UserDto getFull(Long id) throws BusinessRuleException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("0002", "Usuario no localizado.", HttpStatus.PRECONDITION_FAILED));

        UserDto dto = userMapper.toDto(user);
        dto.setPersons(businessTransactions.getPerson(user.getId()));
        return dto;
    }

    @Override
    public List<UserDto> getFullList(Long id) throws BusinessRuleException {
        UserDto userFull = getFull(id);
        return List.of(userFull);
    }

    @Override
    public UserDto createUser(User user) throws BusinessRuleException {
        if (user == null) {
            throw new BusinessRuleException("0001", "El usuario no puede ser nulo.", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessRuleException("0004", "El nombre de usuario ya existe.", HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessRuleException("0005", "El correo electrónico ya existe.", HttpStatus.CONFLICT);
        }

        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) throws BusinessRuleException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("0002", "Usuario no encontrado.", HttpStatus.PRECONDITION_FAILED));

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            existingUser.setUsername(userDto.getUsername().trim());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            existingUser.setEmail(userDto.getEmail().trim());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(userDto.getPassword());
        }
        existingUser.setStatus(true);

        User savedUser = userRepository.save(existingUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) throws BusinessRuleException {
        if (!userRepository.existsById(id)) {
            throw new BusinessRuleException("0002", "Usuario no encontrado.", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String username) {
        return userRepository.existsByEmail(username);
    }
}
