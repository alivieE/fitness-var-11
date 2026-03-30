package com.fitness.booking.adapter.persistence;

import com.fitness.booking.application.port.out.BookingRepository;
import com.fitness.booking.domain.model.Booking;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Outbound adapter: stores Booking objects in a HashMap.
 */
public class InMemoryBookingRepository implements BookingRepository {

    private final Map<UUID, Booking> store = new HashMap<>();

    @Override
    public void save(Booking booking) {
        store.put(booking.getId(), booking);
    }

    @Override
    public Optional<Booking> findByUserAndTraining(String userId, UUID trainingId) {
        return store.values().stream()
            .filter(b -> b.getUserId().equals(userId)
                      && b.getTrainingId().equals(trainingId))
            .findFirst();
    }

    @Override
    public List<Booking> findByUser(String userId) {
        return store.values().stream()
            .filter(b -> b.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
}
