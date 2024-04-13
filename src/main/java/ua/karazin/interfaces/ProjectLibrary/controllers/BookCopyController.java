package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.BookCopiesToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookNotRegisteredException;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;


@RestController
@RequiredArgsConstructor
@RequestMapping("/book_copy")
public class BookCopyController {
    private final BookCopyService bookCopyService;
    private final BookService bookService;

    @PostMapping("/add_book_copies")
    public ResponseEntity<HttpStatus> addBookCopies(@RequestBody @Valid BookCopiesToAddDTO bookCopiesToAddDTO,
                                              BindingResult bindingResult) {
        System.out.println(bookCopiesToAddDTO.toString());

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }

        if(bookService.findBookByIsbn(bookCopiesToAddDTO.isbn()).isEmpty())  // Should it be on a service layer?
            throw new BookNotRegisteredException();

        bookCopyService.addBookCopies(new BookCopy(bookCopiesToAddDTO.isbn(), "free"), bookCopiesToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }


}
