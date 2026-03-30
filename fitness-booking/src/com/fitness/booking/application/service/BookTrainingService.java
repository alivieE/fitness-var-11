package com.fitness.booking.application.service;

import com.fitness.booking.application.dto.BookTrainingCommand;
import com.fitness.booking.application.dto.BookTrainingResult;
import com.fitness.booking.application.port.in.BookTrainingUseCase;
import com.fitness.booking.application.port.out.BookingRepository;
import com.fitness.booking.application.port.out.TrainingRepository;
import com.fitness.booking.domain.exception.TrainingNotFoundException;
import com.fitness.booking.domain.exception.UserAlreadyBookedException;
import com.fitness.booking.domain.model.Booking;
import com.fitness.booking.domain.model.Training;

import java.time.format.DateTimeFormatter;

/**
 * Application service: implements the BookTrainingUseCase port.
 *
 * Orchestrates domain objects and outbound ports.
 * Contains NO infrastructure code — only use-case logic.
 * Depends only on abstractions (interfaces), never on concrete adapters.
 */
public class BookTrainingService implements BookTrainingUseCase {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TrainingRepository trainingRepository;
    private final BookingRepository bookingRepository;

    public BookTrainingService(TrainingRepository trainingRepository,
                               BookingRepository bookingRepository) {
        this.trainingRepository = trainingRepository;
        this.bookingRepository  = bookingRepository;
    }

    @Override
    public BookTrainingResult execute(BookTrainingCommand command) {

        // 1. Load the training — throw if not found
        Training training = trainingRepository
            .findById(command.getTrainingId())
            .orElseThrow(() -> new TrainingNotFoundException(command.getTrainingId()));

        // 2. Business rule: no duplicate bookings per user
        bookingRepository
            .findByUserAndTraining(command.getUserId(), command.getTrainingId())
            .ifPresent(existing -> {
                throw new UserAlreadyBookedException(
                    command.getUserId(), command.getTrainingId());
            });

        // 3. Business rules: not passed + has spots (delegated to entity)
        training.reserveSpot();

        // 4. Persist changes
        Booking booking = Booking.create(command.getUserId(), command.getTrainingId());
        bookingRepository.save(booking);
        trainingRepository.save(training);

        return new BookTrainingResult(
            booking.getId(),
            booking.getUserId(),
            training.getTitle(),
            training.getTrainer(),
            training.getScheduledAt().format(FORMATTER),
            training.availableSpots()
        );
    }
}
