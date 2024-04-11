package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.karazin.interfaces.ProjectLibrary.dto.BookToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final ModelMapper modelMapper;
    private final BookService bookService;

    @PostMapping("/add-book")
    public ResponseEntity<HttpStatus> addBook(@RequestBody @Valid BookToAddDTO bookToAddDTO,
                                              BindingResult bindingResult) {
        System.out.println(bookToAddDTO);
        //Book bookToAdd = convertToBook(bookToAddDTO);
        /*bookValidator.validate(bookToAdd, bindingResult);*/
        //System.out.println(bookToAdd);

        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }

        bookService.addBook(bookToAddDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    /*private Book convertToBook(BookToAddDTO bookToAddDTO) {
        var isbn = bookToAddDTO.isbn();
        var title = bookToAddDTO.title();
        var yearOfPublishing = bookToAddDTO.yearOfPublishing();
        var numberOfPages = bookToAddDTO.numberOfPages();
        var annotation = bookToAddDTO.annotation();
        var language = bookToAddDTO.language();
        var bookPhoto = bookToAddDTO.bookPhoto();

        return new Book();
    }*/

}
