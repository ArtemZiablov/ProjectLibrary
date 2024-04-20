package ua.karazin.interfaces.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.BookDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.BookInfoDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.SearchedBooksDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoSearchedParametersWereProvidedException;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.utils.BookMapper;

import java.util.List;
import java.util.Optional;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@Slf4j(topic = "BookController")
@RequiredArgsConstructor
@RequestMapping("/book")
@RestController
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping("/add-book")
    public ResponseEntity<HttpStatus> addBook(@RequestBody @Valid BookDTO bookToAddDTO,
                                              BindingResult bindingResult) {
        log.info("Req to /book/add_book : {}", bookToAddDTO);

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
        if (title.isPresent() && title.get().trim().length() >= 4) {
            log.info("Req to /book/search?title={}", title);
            res = bookService.findBooksByTitleStartingWith(title.get().trim());
        } else if (author.isPresent() && author.get().trim().length() >= 4) {
            log.info("Req to /book/search?author={}", author);
            res = bookService.findBooksByAuthorsNameStartingWith(author.get().trim());
        } else if (genre.isPresent() && genre.get().trim().length() >= 4) {
            log.info("Req to /book/search?genre={}", genre);
            res = bookService.findBooksByGenreStartingWith(genre.get().trim());
        } else {
            throw new NoSearchedParametersWereProvidedException();
        }

        return bookMapper.mapToSearchedBookDTO(res);
    }

    @GetMapping("/get-book-by-isbn")
    public BookInfoDTO viewBookInfo(@RequestParam(value = "isbn") Optional<Integer> isbn) {
        log.info("Req to /book/get-book-by-isbn?isbn={}", isbn);
        return isbn.flatMap(bookService::findBookByIsbn)
                .map(bookMapper::mapToBookInfoDTO)
                .orElseThrow(NoSearchedParametersWereProvidedException::new);
    }

    // TODO: reserve-book
}
