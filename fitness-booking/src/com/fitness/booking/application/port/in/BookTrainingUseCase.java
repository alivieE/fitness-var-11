package com.fitness.booking.application.port.in;

import com.fitness.booking.application.dto.BookTrainingCommand;
import com.fitness.booking.application.dto.BookTrainingResult;

/**
 * Inbound port — defines what the application can do from the outside.
 * The CLI adapter (or any other driver) depends on this interface only.
 */
public interface BookTrainingUseCase {
    BookTrainingResult execute(BookTrainingCommand command);
}
