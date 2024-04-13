package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.BookToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.utils.BookMapper;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookCopyController bookCopyController;
    private final BookMapper bookMapper;

    @PostMapping("/add_book")
    public ResponseEntity<HttpStatus> addBook(@RequestBody @Valid BookToAddDTO bookToAddDTO,
                                              BindingResult bindingResult) {
        System.out.println(bookToAddDTO.toString());
        //Book bookToAdd = convertToBook(bookToAddDTO);
        /*bookValidator.validate(bookToAdd, bindingResult);*/

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }

        Book book = bookMapper.mapToBook(bookToAddDTO);
        bookService.addBook(book, bookToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
