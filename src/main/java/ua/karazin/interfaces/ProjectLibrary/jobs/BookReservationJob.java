package ua.karazin.interfaces.ProjectLibrary.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.models.BookReservation;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.EmailService;

import java.util.List;

@Slf4j(topic = "BookReservationJob")
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BookReservationJob {
    private final BookReservationService bookReservationService;
    private final BookProperties bookProperties;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 1 * * ?")  // repeating every day at 1:00 am
    public void execute() {
        log.info("Running scheduled job to check and remove expired book reservations.");
        try {
            // Fetch all reservations that are two days old or more
            List<BookReservation> expiredReservations = bookReservationService.findExpiredActiveReservations(bookProperties.reservationDays());

            // Iterate over expired reservations and remove them
            for (BookReservation reservation : expiredReservations) {
                bookReservationService.removeReservation(reservation);
                log.info("Removed expired reservation: {}", reservation);
                var oldestReservation = bookReservationService.getOldestActiveReservationByIsbn(reservation.getBook().getIsbn());
                if (oldestReservation != null) {
                    oldestReservation.setStatus(bookProperties.activeReservationStatus());
                    var reader = oldestReservation.getReader();
                    String message = String.format("Your reservation for book %s was activated. You have two days to pick up it from the library. Cheers", reservation.getBook().getTitle());
                    emailService.sendSimpleEmail(reader.getEmail(), "LMS", message);
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while executing the book reservation cleanup job", e);
        }
    }
}
