package com.fitness.booking.application.dto;

import java.util.UUID;

/**
 * Command object: carries input data into the use case.
 * Keeps the use case interface clean and extensible.
 */
public class BookTrainingCommand {
    private final String userId;
    private final UUID trainingId;

    public BookTrainingCommand(String userId, UUID trainingId) {
        this.userId = userId;
        this.trainingId = trainingId;
    }

    public String getUserId()      { return userId; }
    public UUID getTrainingId()    { return trainingId; }
}
