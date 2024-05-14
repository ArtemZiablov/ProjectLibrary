package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.exceptions.*;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

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

    @PostMapping("/reserve-book")
    public ResponseEntity<HttpStatus> reserveBook(@RequestParam(value = "isbn") Optional<Long> isbn,
                                                  Authentication authentication) {
        if (isbn.isEmpty()) {
            throw new NoRequestedParametersWereProvidedException();
        } else {

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

                bookReservationService.addReservation(book, reader);
            }
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
}


