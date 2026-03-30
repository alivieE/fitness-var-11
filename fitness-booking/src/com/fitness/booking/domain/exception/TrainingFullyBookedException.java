package com.fitness.booking.domain.exception;

public class TrainingFullyBookedException extends DomainException {
    public TrainingFullyBookedException(String title) {
        super("Training '" + title + "' has no available spots.");
    }
}
