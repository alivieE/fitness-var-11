package com.fitness.booking.domain.exception;

import java.util.UUID;

public class TrainingNotFoundException extends DomainException {
    public TrainingNotFoundException(UUID id) {
        super("Training with id '" + id + "' was not found.");
    }
}
