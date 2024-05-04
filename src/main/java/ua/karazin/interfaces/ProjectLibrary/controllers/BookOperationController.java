package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.StatisticsDTO;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "BookOperationController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/book-operation")
public class BookOperationController {

    private final BookService bookService;
    private final ReaderService readerService;

    // TODO add endpoints for statistics functional...
    @GetMapping("/statistics")
    public StatisticsDTO getStatistics() { // кількисть книг всього, кількість всього взятих книг
        Map<String, Map<String,Integer>> booksStatistics = new HashMap<>();
        Map<String, Map<String,Integer>> readersStatistics = new HashMap<>();;

        booksStatistics.put("booksStatistics", bookService.getBooksStatistics());
        readersStatistics.put("readersStatistics", readerService.getReadersStatistics());

        return new StatisticsDTO(booksStatistics, readersStatistics);
    }
}
