package com.fitness.booking.application.port.out;

import com.fitness.booking.domain.model.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port — how the application talks to the storage layer.
 * The use case depends on this interface; adapters implement it.
 */
public interface TrainingRepository {
    Optional<Training> findById(UUID id);
    List<Training> findAll();
    void save(Training training);
}
