package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.karazin.interfaces.ProjectLibrary.dto.StatisticsDTO;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.Map;

@Slf4j(topic = "BookOperationController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/book-operation")
public class BookOperationController {

    private final BookService bookService;
    private final ReaderService readerService;

    // TODO add endpoints for statistics functional...
    @GetMapping("/statistics")
    public StatisticsDTO getStatistics() {
        Map<String, Integer> booksStatistics = bookService.getBooksStatistics();
        Map<String, Integer> readersStatistics = readerService.getReadersStatistics();

        return new StatisticsDTO(booksStatistics, readersStatistics);
    }
}
