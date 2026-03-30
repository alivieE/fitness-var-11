package com.fitness.booking.domain.exception;

public class TrainingAlreadyPassedException extends DomainException {
    public TrainingAlreadyPassedException(String title) {
        super("Training '" + title + "' has already passed and cannot be booked.");
    }
}
