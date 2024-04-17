package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.BookDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.SearchBookDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.SearchedBooksDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoSearchedParametersWereProvidedException;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.utils.BookMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping("/add_book")
    public ResponseEntity<HttpStatus> addBook(@RequestBody @Valid BookDTO bookToAddDTO,
                                              BindingResult bindingResult) {
        log.info("Req to /add_book : {}", bookToAddDTO);

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        Book bookToAdd = bookMapper.mapToBook(bookToAddDTO);
        bookService.addBook(bookToAdd, bookToAddDTO.copiesAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/search")
    public SearchedBooksDTO searchBooks(@RequestParam(value = "title") Optional<String> title,
                                        @RequestParam(value = "author") Optional<String> author,
                                        @RequestParam(value = "genre") Optional<String> genre){

        List<Book> res;
        if (title.isPresent()) {
            log.info("Req to /search?title={}", title);
            res = bookService.findBooksByTitleStartingWith(title.get());
            return bookMapper.mapToSearchedBookDTO(res);
        } else if (author.isPresent()){
            log.info("Req to /search?author={}", author);
            res = bookService.findBooksByAuthorsNameStartingWith(author.get());
            return bookMapper.mapToSearchedBookDTO(res);
        } else if (genre.isPresent()){
            log.info("Req to /search?genre={}", genre);
            res = bookService.findBooksByGenreStartingWith(genre.get());
            return bookMapper.mapToSearchedBookDTO(res);
        } else
            throw new NoSearchedParametersWereProvidedException();

    }

}
