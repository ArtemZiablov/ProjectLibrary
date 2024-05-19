package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.dto.BookCopiesToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.OperationWithBookCopyDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopyDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.*;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.models.BookReservation;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;
import ua.karazin.interfaces.ProjectLibrary.services.*;

import java.util.List;
import java.util.Optional;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@Slf4j(topic = "BookCopyController")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/book-copy")
public class BookCopyController {
    private final BookCopyService bookCopyService;
    private final BookService bookService;
    private final ReaderService readerService;
    private final BookProperties bookProperties;
    private final BookOperationService bookOperationService;
    private final LibrarianService librarianService;
    private final BookReservationService bookReservationService;

    @PostMapping("/add-book-copies")
    public ResponseEntity<HttpStatus> addBookCopies(@RequestBody @Valid BookCopiesToAddDTO bookCopiesToAddDTO,
                                                    BindingResult bindingResult) {

        log.info("Req to /book-copy/add_book_copies : {}", bookCopiesToAddDTO);

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        var book = bookService.findBookByIsbn(bookCopiesToAddDTO.isbn());
        if (book.isEmpty())
            throw new BookNotRegisteredException();

        bookCopyService.addBookCopies(new BookCopy(book.get(), "free"), bookCopiesToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete-book-copy")
    public ResponseEntity<HttpStatus> deleteBookCopy(
            @RequestParam("isbn") Optional<Long> isbn,
            @RequestParam("copyId") Optional<Integer> copyId
    ) {
        log.info("Req to /book-copy/delete_book_copy isbn= {}; copyId= {}", isbn, copyId);

        if (copyId.isEmpty() || isbn.isEmpty())
            throw new NoRequestedParametersWereProvidedException();
        else {
            var bookCopy = bookCopyService.findBookCopyByCopyId(copyId.get()).orElseThrow(BookCopyNotExistException::new);
            if (bookCopy.getBook().getIsbn().equals(isbn.get())) {
                bookCopyService.deleteBookCopy(copyId.get());
            } else throw new WrongIsbnException();
            ;
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/assign-book-copies")
    public ResponseEntity<HttpStatus> assignBookCopies(@RequestBody @Valid OperationWithBookCopyDTO assignBookCopiesDTO,
                                                       Authentication authentication) {
        log.info("Req to /book-copy/assign-book-copies : {}", assignBookCopiesDTO);

        Librarian librarian;
        
        if (authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            librarian = librarianDetails.librarian();
        } else
            throw new NotAuthenticatedException();

        var reader = readerService.findReaderById(assignBookCopiesDTO.readerId()).orElseThrow(ReaderNotExistException::new);
        List<BookCopy> bookCopies = bookCopyService.findBookCopiesByListOfCopiesId(assignBookCopiesDTO.bookCopiesId());

        var readersBookAmount = (long) reader.getBookCopies().size() + assignBookCopiesDTO.bookCopiesId().size();
        if (readersBookAmount > bookProperties.bookAssignAmount()) {
            System.out.println(readersBookAmount);
            throw new AssignBookLimitOutOfBoundsException();
        } else {
            for (BookCopy bookCopy : bookCopies) {
                if (bookOperationService.findOpenBookOperationByReaderIdAndBookIsbn(assignBookCopiesDTO.readerId(), bookCopy.getBook().getIsbn()).isEmpty())
                    bookCopyService.assignBookCopy(bookCopy, reader, librarian);
                else
                    throw new BookIsAlreadyTakenByReaderException();
            }
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/release-book-copy") // TODO: refactor
    public ResponseEntity<HttpStatus> releaseBook(@RequestBody @Valid OperationWithBookCopyDTO releaseBookCopyDTO) {

        log.info("Req to /book-copy/release_book_copy : {}", releaseBookCopyDTO);

        var reader = readerService.findReaderById(releaseBookCopyDTO.readerId()).orElseThrow(ReaderNotExistException::new);
        var bookCopy = bookCopyService.findBookCopyByCopyId(releaseBookCopyDTO.bookCopiesId().get(0)).orElseThrow(BookCopyNotExistException::new);

        long isbn = bookCopy.getBook().getIsbn();

        bookCopyService.releaseBookCopy(bookCopy, reader);

        int countAvailableBookCopies = bookCopyService.countAvailableBookCopies(isbn);
        log.info("countAvailableBookCopies = {}", countAvailableBookCopies);
        int countAwaitReservations = bookReservationService.countAwaitReservationsByIsbn(isbn);
        log.info("countAwaitReservations = {}", countAwaitReservations);

        if ( /*еще кто-то ждет резервацию*/ countAwaitReservations > 0) {
            BookReservation oldestReservation = bookReservationService.getOldestActiveReservationByIsbn(isbn);
            bookReservationService.activateReservation(oldestReservation);
            //sendEmail шо у тебя осталось два дня резервации
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get-readers-books")
    public List<ReadersBookCopyDTO> getReadersBooks(@RequestParam(name = "readerId") Integer readerId) { // TODO: deprecate
        log.info("Req to /book-copy/get_readers_books with readerID: {}", readerId);

        return bookCopyService.getReadersBookCopies(readerId);
    }

}
