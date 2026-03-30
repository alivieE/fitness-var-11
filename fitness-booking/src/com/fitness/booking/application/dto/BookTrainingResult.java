package com.fitness.booking.application.dto;

import java.util.UUID;

/**
 * Result object: carries output data out of the use case.
 * The adapter (CLI, REST, etc.) formats this however it wants.
 */
public class BookTrainingResult {
    private final UUID bookingId;
    private final String userId;
    private final String trainingTitle;
    private final String trainer;
    private final String scheduledAt;
    private final int spotsRemaining;

    public BookTrainingResult(UUID bookingId, String userId, String trainingTitle,
                               String trainer, String scheduledAt, int spotsRemaining) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.trainingTitle = trainingTitle;
        this.trainer = trainer;
        this.scheduledAt = scheduledAt;
        this.spotsRemaining = spotsRemaining;
    }

    public UUID getBookingId()       { return bookingId; }
    public String getUserId()        { return userId; }
    public String getTrainingTitle() { return trainingTitle; }
    public String getTrainer()       { return trainer; }
    public String getScheduledAt()   { return scheduledAt; }
    public int getSpotsRemaining()   { return spotsRemaining; }
}
