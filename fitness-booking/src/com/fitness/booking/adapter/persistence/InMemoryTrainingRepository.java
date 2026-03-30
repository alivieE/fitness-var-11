package com.fitness.booking.adapter.persistence;

import com.fitness.booking.application.port.out.TrainingRepository;
import com.fitness.booking.domain.model.Training;

import java.util.*;

/**
 * Outbound adapter: stores Training objects in a HashMap.
 *
 * Implements the TrainingRepository port.
 * Can be replaced by a JPA or JDBC adapter without touching the use case.
 */
public class InMemoryTrainingRepository implements TrainingRepository {

    private final Map<UUID, Training> store = new HashMap<>();

    @Override
    public Optional<Training> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Training training) {
        store.put(training.getId(), training);
    }
}
