package com.fitness.booking.domain.model;

import com.fitness.booking.domain.exception.TrainingAlreadyPassedException;
import com.fitness.booking.domain.exception.TrainingFullyBookedException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity. Contains business rules about a training session.
 * No framework annotations, no persistence details — pure business logic.
 */
public class Training {

    private final UUID id;
    private final String title;
    private final String trainer;
    private final LocalDateTime scheduledAt;
    private final int capacity;
    private int bookedCount;

    public Training(UUID id, String title, String trainer,
                    LocalDateTime scheduledAt, int capacity) {
        this.id = id;
        this.title = title;
        this.trainer = trainer;
        this.scheduledAt = scheduledAt;
        this.capacity = capacity;
        this.bookedCount = 0;
    }

    // --- Business rules ---

    public boolean isAvailable() {
        return bookedCount < capacity;
    }

    public boolean isInFuture() {
        return scheduledAt.isAfter(LocalDateTime.now());
    }

    public int availableSpots() {
        return capacity - bookedCount;
    }

    /**
     * Business rule: reserves one spot on this training.
     * Throws domain exceptions if booking is not allowed.
     */
    public void reserveSpot() {
        if (!isInFuture()) {
            throw new TrainingAlreadyPassedException(title);
        }
        if (!isAvailable()) {
            throw new TrainingFullyBookedException(title);
        }
        bookedCount++;
    }

    // --- Getters ---

    public UUID getId()                  { return id; }
    public String getTitle()             { return title; }
    public String getTrainer()           { return trainer; }
    public LocalDateTime getScheduledAt(){ return scheduledAt; }
    public int getCapacity()             { return capacity; }
    public int getBookedCount()          { return bookedCount; }
}
