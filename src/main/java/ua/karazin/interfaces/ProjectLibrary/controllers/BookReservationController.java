package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.exceptions.*;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j(topic = "BookReservationController")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/book-reservation")
@RestController
public class BookReservationController {
    private final BookReservationService bookReservationService;
    private final ReaderService readerService;
    private final BookService bookService;
    private final BookProperties bookProperties;
    private final BookCopyService bookCopyService;

    @PostMapping("/reserve-book")
    public Map<String, String> reserveBook(@RequestParam(value = "isbn") Optional<Long> isbn,
                                           Authentication authentication) {
        if (isbn.isEmpty()) {
            throw new NoRequestedParametersWereProvidedException();
        } else {
            Map<String, String> res = new HashMap<>();

            if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
                int readerID = readerDetails.reader().getId();

                var reader = readerService.findReaderById(readerID).orElseThrow(ReaderNotExistException::new);
                if (reader.isDebtor())
                    throw new ReaderIsDebtorException();
                var book = bookService.findBookByIsbn(isbn.get()).orElseThrow(BookNotRegisteredException::new);
                if (bookReservationService.findBookReservationByBookAndReader(book, reader).isPresent())
                    throw new BookReservationAlreadyExistException();

                var count = bookReservationService.countBookReservationsByReaderId(reader.getId());
                System.out.println(count + " " + bookProperties.bookReserveAmount());
                if (count > bookProperties.bookReserveAmount() - 1)
                    throw new ReservationBookLimitOutOfBoundsException();

                String status;
                if (bookCopyService.countAvailableBookCopies(isbn.get()) > 0)
                    status = bookProperties.activeReservationStatus();
                else
                    status = bookProperties.awaitReservationStatus();
                bookReservationService.addReservation(book, reader, status);

                res.put("response", "Book has been successfully reserved, you have two days to pick it up");
                return res;
            }
            res.put("response", "You will receive an email when you can get your book");
            return res;
        }
    }
}


