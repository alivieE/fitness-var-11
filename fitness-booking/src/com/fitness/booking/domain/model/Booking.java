package com.fitness.booking.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity: a confirmed reservation of a user for a specific training.
 */
public class Booking {

    private final UUID id;
    private final String userId;
    private final UUID trainingId;
    private final BookingStatus status;
    private final LocalDateTime createdAt;

    private Booking(UUID id, String userId, UUID trainingId,
                    BookingStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.trainingId = trainingId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Booking create(String userId, UUID trainingId) {
        return new Booking(
            UUID.randomUUID(),
            userId,
            trainingId,
            BookingStatus.CONFIRMED,
            LocalDateTime.now()
        );
    }

    public UUID getId()           { return id; }
    public String getUserId()     { return userId; }
    public UUID getTrainingId()   { return trainingId; }
    public BookingStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
