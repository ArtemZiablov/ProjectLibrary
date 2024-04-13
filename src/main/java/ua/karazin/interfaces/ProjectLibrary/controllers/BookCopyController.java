package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.BookCopiesToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.BookCopyToDeleteDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookNotRegisteredException;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book_copy")
public class BookCopyController {
    private final BookCopyService bookCopyService;
    private final BookService bookService;

    @PostMapping("/add_book_copies")
    public ResponseEntity<HttpStatus> addBookCopies(@RequestBody @Valid BookCopiesToAddDTO bookCopiesToAddDTO,
                                              BindingResult bindingResult) {

        log.info("Req to /add_book_copies : {}", bookCopiesToAddDTO);

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }

        if(bookService.findBookByIsbn(bookCopiesToAddDTO.isbn()).isEmpty())  // TODO Should it be on a service layer or in validator class?
            throw new BookNotRegisteredException();

        bookCopyService.addBookCopies(new BookCopy(bookCopiesToAddDTO.isbn(), "free"), bookCopiesToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/delete_book_copy")
    public ResponseEntity<HttpStatus> deleteBookCopy(@RequestBody @Valid BookCopyToDeleteDTO bookCopyToDeleteDTO,
                                                     BindingResult bindingResult){
        // TODO
        log.info("Req to /delete_book_copy : {}", bookCopyToDeleteDTO);

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }

        bookCopyService.deleteBookCopy(bookCopyToDeleteDTO.copyId());


        return ResponseEntity.ok(HttpStatus.OK);
    }


}
