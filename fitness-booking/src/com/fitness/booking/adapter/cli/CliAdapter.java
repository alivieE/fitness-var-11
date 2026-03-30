package com.fitness.booking.adapter.cli;

import com.fitness.booking.application.dto.BookTrainingCommand;
import com.fitness.booking.application.dto.BookTrainingResult;
import com.fitness.booking.application.port.in.BookTrainingUseCase;
import com.fitness.booking.domain.exception.DomainException;

import java.util.UUID;

/**
 * Inbound adapter: drives the application from the command line.
 *
 * Depends only on the BookTrainingUseCase inbound port.
 * Knows nothing about domain internals or persistence.
 */
public class CliAdapter {

    private final BookTrainingUseCase bookTrainingUseCase;

    public CliAdapter(BookTrainingUseCase bookTrainingUseCase) {
        this.bookTrainingUseCase = bookTrainingUseCase;
    }

    public void runBooking(String userId, UUID trainingId) {
        System.out.println("\n--- Booking request ---");
        System.out.println("  User     : " + userId);
        System.out.println("  Training : " + trainingId);

        try {
            BookTrainingResult result = bookTrainingUseCase.execute(
                new BookTrainingCommand(userId, trainingId)
            );
            System.out.println("\n  Booking confirmed!");
            System.out.println("  Booking ID  : " + result.getBookingId());
            System.out.println("  Training    : " + result.getTrainingTitle());
            System.out.println("  Trainer     : " + result.getTrainer());
            System.out.println("  Date & time : " + result.getScheduledAt());
            System.out.println("  Spots left  : " + result.getSpotsRemaining());

        } catch (DomainException e) {
            System.out.println("\n  Booking failed: " + e.getMessage());
        }
    }
}
