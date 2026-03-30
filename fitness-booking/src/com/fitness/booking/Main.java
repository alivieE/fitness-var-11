package com.fitness.booking;

import com.fitness.booking.adapter.cli.CliAdapter;
import com.fitness.booking.adapter.persistence.InMemoryBookingRepository;
import com.fitness.booking.adapter.persistence.InMemoryTrainingRepository;
import com.fitness.booking.application.port.out.BookingRepository;
import com.fitness.booking.application.port.out.TrainingRepository;
import com.fitness.booking.application.service.BookTrainingService;
import com.fitness.booking.domain.model.Training;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Composition Root — the only place that knows about concrete implementations.
 * Wires adapters → ports → use case and runs demo scenarios.
 */
public class Main {

    public static void main(String[] args) {

        // --- Wiring (Dependency Injection by hand) ---
        TrainingRepository trainingRepo = new InMemoryTrainingRepository();
        BookingRepository  bookingRepo  = new InMemoryBookingRepository();

        BookTrainingService service = new BookTrainingService(trainingRepo, bookingRepo);
        CliAdapter cli = new CliAdapter(service);

        // --- Seed sample data ---
        LocalDateTime now = LocalDateTime.now();

        Training yoga = new Training(
            UUID.randomUUID(), "Morning Yoga", "Anna K.",
            now.plusDays(1).withHour(8).withMinute(0), 10
        );
        Training crossfit = new Training(
            UUID.randomUUID(), "CrossFit Basics", "Dmytro V.",
            now.plusDays(2).withHour(18).withMinute(0), 2   // only 2 spots
        );
        Training stretching = new Training(
            UUID.randomUUID(), "Evening Stretching", "Olena S.",
            now.minusHours(3), 15                            // already passed
        );

        trainingRepo.save(yoga);
        trainingRepo.save(crossfit);
        trainingRepo.save(stretching);

        System.out.println("=".repeat(55));
        System.out.println("   FITNESS BOOKING — demo scenarios");
        System.out.println("=".repeat(55));

        // 1. Successful booking
        cli.runBooking("alice", yoga.getId());

        // 2. Duplicate booking — same user, same training
        cli.runBooking("alice", yoga.getId());

        // 3. Another user books the same training
        cli.runBooking("bob", yoga.getId());

        // 4. Fill CrossFit to capacity, then one more attempt
        cli.runBooking("alice",  crossfit.getId());
        cli.runBooking("bob",    crossfit.getId());
        cli.runBooking("carol",  crossfit.getId());  // fully booked

        // 5. Attempt to book a past training
        cli.runBooking("alice", stretching.getId());

        System.out.println("\n" + "=".repeat(55));
    }
}
