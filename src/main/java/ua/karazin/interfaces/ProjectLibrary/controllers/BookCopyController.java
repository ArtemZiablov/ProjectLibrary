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
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.services.LibrarianService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book_copy")
public class BookCopyController {
    private final BookCopyService bookCopyService;
    private final BookService bookService;
    private final ReaderService readerService;
    private final BookProperties bookProperties;
    private final LibrarianService librarianService;

    @PostMapping("/add_book_copies")
    public ResponseEntity<HttpStatus> addBookCopies(@RequestBody @Valid BookCopiesToAddDTO bookCopiesToAddDTO,
                                              BindingResult bindingResult) {

        log.info("Req to /add_book_copies : {}", bookCopiesToAddDTO);

        // TODO validate

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        var book = bookService.findBookByIsbn(bookCopiesToAddDTO.isbn());
        if(book.isEmpty())  // TODO Should it be on a service layer or in validator class?
            throw new BookNotRegisteredException();

        bookCopyService.addBookCopies(new BookCopy(book.get(), "free"), bookCopiesToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete_book_copy")
    public ResponseEntity<HttpStatus> deleteBookCopy(@RequestBody @Valid BookCopyToDeleteDTO bookCopyToDeleteDTO,
                                                     BindingResult bindingResult){
        log.info("Req to /delete_book_copy : {}", bookCopyToDeleteDTO);

        // TODO validate

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        bookCopyService.deleteBookCopy(bookCopyToDeleteDTO.copyId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/assign_book_copy")
    public ResponseEntity<HttpStatus> assignBookCopy(@RequestBody @Valid OperationWithBookCopyDTO assignBookCopyDTO){
        log.info("Req to /assign_book_copy : {}", assignBookCopyDTO);

        var reader = readerService.findReaderById(assignBookCopyDTO.readerId());
        var bookCopy = bookCopyService.findBookCopyByCopyId(assignBookCopyDTO.bookCopyId());

        if ((long) reader.get().getBookCopies().size() >= bookProperties.bookAssignAmount())
            throw new AssignBookLimitOutOfBoundsException();
        else {
            // TODO: add librarian using Spring Security
            var librarian = librarianService.findLibrarianById(1);
            bookCopyService.assignBookCopy(bookCopy.get(), reader.get(), librarian.get());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/release_book_copy")
    public ResponseEntity<HttpStatus> releaseBook(@RequestBody @Valid OperationWithBookCopyDTO releaseBookCopyDTO){

        log.info("Req to /release_book_copy : {}", releaseBookCopyDTO);

        var reader = readerService.findReaderById(releaseBookCopyDTO.readerId());
        var bookCopy = bookCopyService.findBookCopyByCopyId(releaseBookCopyDTO.bookCopyId());

        // подразумеваем, что мы уже нашли ридера в конроллера Reader и получили список всех его книг

        bookCopyService.releaseBookCopy(bookCopy.get(), reader.get());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get_readers_books")
    public ReadersBookCopiesDTO getReadersBooks(@RequestParam(name = "readerId") Integer readerId){
        log.info("Req to /get_readers_books with readerID: {}", readerId);

        var books = bookCopyService.getReadersBookCopies(readerId);

        return books;
    }

}
