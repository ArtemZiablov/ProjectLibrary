package ua.karazin.interfaces.ProjectLibrary.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.karazin.interfaces.ProjectLibrary.models.BookOperation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.services.BookOperationService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.Date;
import java.util.List;

@Slf4j(topic = "BookReturnDeadlineJob")
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BookReturnDeadlineJob{

    private final BookOperationService bookOperationService;
    private final ReaderService readerService;

    @Scheduled(cron = "0 0 1 * * ?")  // repeating every day at 1:00 am
    public void execute(){
        log.info("Job started executing.");

            Date today = new Date();
            List<BookOperation> operations = bookOperationService.findBookOperationsByDateOfReturnIsNull();
            for (BookOperation operation : operations) {
                Reader reader = operation.getReader();
                if (operation.getReturnDeadline() != null && operation.getReturnDeadline().before(today) && !reader.isDebtor()) {
                    reader.setDebtor(true);
                    readerService.save(reader);
                    log.info("Updated debtor status for reader ID: {}", reader.getId());
                }

            }

        log.info("Job finished executing.");
    }
}
