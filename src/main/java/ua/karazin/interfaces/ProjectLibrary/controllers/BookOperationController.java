package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.karazin.interfaces.ProjectLibrary.dto.SearchReaderDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.StatisticsDTO;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.services.BookOperationService;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.List;
import java.util.Map;

@Slf4j(topic = "BookOperationController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/book-operation")
public class BookOperationController {

    private final BookService bookService;
    private final ReaderService readerService;
    private final BookOperationService bookOperationService;
    private final BookReservationService bookReservationService;

    // TODO add endpoints for statistics functional...
    @GetMapping("/statistics")
    public StatisticsDTO getStatistics() {
        Map<String, Integer> booksStatistics = bookService.getBooksStatistics();
        Map<String, Integer> readersStatistics = readerService.getReadersStatistics();

        return new StatisticsDTO(booksStatistics, readersStatistics);
    }

    @GetMapping("/readers-who-reserved-books")
    public List<SearchReaderDTO> getReadersWhoReservedBooks() {
        return mapToListSearchReaderDTO(bookReservationService.getReadersWhoReservedBooks());
    }

    @GetMapping("/debtors")
    public List<SearchReaderDTO> getDebtors() {
        return mapToListSearchReaderDTO(readerService.getDebtors());
    }

    @GetMapping("/ongoing-readers")
    public List<SearchReaderDTO> getOngoingReader() {
        return mapToListSearchReaderDTO(bookOperationService.getOngoingReaders());
    }

    private List<SearchReaderDTO> mapToListSearchReaderDTO(List<Reader> readers) {
        return readers.stream().map(reader ->
                new SearchReaderDTO(
                        reader.getId(),
                        reader.getFullName(),
                        reader.getPhoneNumber(),
                        reader.getProfilePhoto()
                )
        ).toList();
    }


}
