package ua.karazin.interfaces.ProjectLibrary.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;

@Slf4j(topic = "BookReservationJob")
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BookReservationJob {
    private final BookReservationService bookReservationService;

    @Scheduled(cron = "0 0 1 * * ?")  // repeating every day at 1:00 am
    public void execute(){
        // TODO: implement
    }
}
