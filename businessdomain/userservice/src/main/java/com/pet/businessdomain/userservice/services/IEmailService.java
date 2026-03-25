package com.pet.businessdomain.userservice.services;

import com.pet.businessdomain.shareddto.dto.UserDto;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendConfirmationEmail(UserDto user) throws MessagingException;
}
