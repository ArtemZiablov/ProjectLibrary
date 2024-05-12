package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.dto.*;
import ua.karazin.interfaces.ProjectLibrary.exceptions.*;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
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

    @PostMapping("/add-book-copies")
    public ResponseEntity<HttpStatus> addBookCopies(@RequestBody @Valid BookCopiesToAddDTO bookCopiesToAddDTO,
                                              BindingResult bindingResult) {

        log.info("Req to /book-copy/add_book_copies : {}", bookCopiesToAddDTO);

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        var book = bookService.findBookByIsbn(bookCopiesToAddDTO.isbn());
        if(book.isEmpty())
            throw new BookNotRegisteredException();

        bookCopyService.addBookCopies(new BookCopy(book.get(), "free"), bookCopiesToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete-book-copy")
    public ResponseEntity<HttpStatus> deleteBookCopy(@RequestParam("copyId") Optional<Integer> copyId){
        log.info("Req to /book-copy/delete_book_copy : {}", copyId);

        if(copyId.isPresent())
            bookCopyService.deleteBookCopy(copyId.get());
        else
            throw new NoRequestedParametersWereProvidedException();

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/assign-book-copies")
    public ResponseEntity<HttpStatus> assignBookCopies(@RequestBody @Valid OperationWithBookCopyDTO assignBookCopiesDTO){
        log.info("Req to /book-copy/assign-book-copies : {}", assignBookCopiesDTO);

        var reader = readerService.findReaderById(assignBookCopiesDTO.readerId()).orElseThrow(ReaderNotExistException::new);
        List<BookCopy> bookCopies = bookCopyService.findBookCopiesByListOfCopiesId(assignBookCopiesDTO.bookCopiesId());

        var readersBookAmount = (long) reader.getBookCopies().size() + assignBookCopiesDTO.bookCopiesId().size();
        if (readersBookAmount > bookProperties.bookAssignAmount()) {
            System.out.println(readersBookAmount);
            throw new AssignBookLimitOutOfBoundsException();
        } else {
            // TODO: add librarian using Spring Security
            var librarian = librarianService.findLibrarianById(1);
            for(BookCopy bookCopy : bookCopies) {
                if(bookOperationService.findOpenBookOperationByReaderIdAndBookIsbn(assignBookCopiesDTO.readerId(), bookCopy.getBook().getIsbn()).isEmpty())
                    bookCopyService.assignBookCopy(bookCopy, reader, librarian.get());
                else
                    throw new BookIsAlreadyTakenByReaderException();
            }
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /*@PostMapping("/assign-book-copy") TODO: deprecate
    public ResponseEntity<HttpStatus> assignBookCopy(@RequestBody @Valid OperationWithBookCopyDTO assignBookCopyDTO){
        log.info("Req to /book-copy/assign_book_copy : {}", assignBookCopyDTO);

        var reader = readerService.findReaderById(assignBookCopyDTO.readerId());
        var bookCopy = bookCopyService.findBookCopyByCopyId(assignBookCopyDTO.bookCopiesId());

        if ((long) reader.get().getBookCopies().size() >= bookProperties.bookAssignAmount())
            throw new AssignBookLimitOutOfBoundsException();
        else {
            // TODO: add librarian using Spring Security
            var librarian = librarianService.findLibrarianById(1);
            bookCopyService.assignBookCopy(bookCopy.get(), reader.get(), librarian.get());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }*/

    @PostMapping("/release-book-copy") // TODO: refactor
    public ResponseEntity<HttpStatus> releaseBook(@RequestBody @Valid OperationWithBookCopyDTO releaseBookCopyDTO){

        log.info("Req to /book-copy/release_book_copy : {}", releaseBookCopyDTO);

        var reader = readerService.findReaderById(releaseBookCopyDTO.readerId());
        var bookCopy = bookCopyService.findBookCopyByCopyId(releaseBookCopyDTO.bookCopiesId().get(0));

        // подразумеваем, что мы уже нашли ридера в конроллера Reader и получили список всех его книг

        bookCopyService.releaseBookCopy(bookCopy.get(), reader.get());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get-readers-books")
    public ReadersBookCopiesDTO getReadersBooks(@RequestParam(name = "readerId") Integer readerId){
        log.info("Req to /book-copy/get_readers_books with readerID: {}", readerId);

        return bookCopyService.getReadersBookCopies(readerId);
    }

}
