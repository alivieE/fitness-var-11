package com.fitness.booking.application.port.out;

import com.fitness.booking.domain.model.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port — how the application talks to booking storage.
 */
public interface BookingRepository {
    void save(Booking booking);
    Optional<Booking> findByUserAndTraining(String userId, UUID trainingId);
    List<Booking> findByUser(String userId);
}
