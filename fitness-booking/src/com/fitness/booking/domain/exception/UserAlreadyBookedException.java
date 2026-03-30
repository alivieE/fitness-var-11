package com.fitness.booking.domain.exception;

import java.util.UUID;

public class UserAlreadyBookedException extends DomainException {
    public UserAlreadyBookedException(String userId, UUID trainingId) {
        super("User '" + userId + "' has already booked training '" + trainingId + "'.");
    }
}
