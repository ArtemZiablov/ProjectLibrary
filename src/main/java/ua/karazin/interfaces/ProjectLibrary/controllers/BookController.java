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
import ua.karazin.interfaces.ProjectLibrary.dto.BookDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.BooksInfoDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.GetListBookDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoNoveltiesWereFoundException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoRequestedParametersWereProvidedException;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookService;
import ua.karazin.interfaces.ProjectLibrary.utils.BookMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.karazin.interfaces.ProjectLibrary.utils.ErrorsUtil.returnErrorsToClient;

@Slf4j(topic = "BookController")
@RequiredArgsConstructor
@RequestMapping("/book")
@RestController
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final BookProperties bookProperties;
    private final BookCopyService bookCopyService;

    @PostMapping("/multiple-add-book") // putMapping
    public ResponseEntity<HttpStatus> multipleAddBook(@RequestBody List<BookDTO> books,
                                                      BindingResult bindingResult) {
        log.info("Req to /book/multiple/add_book");

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        for (BookDTO book : books) {
            Book bookToAdd = bookMapper.mapToBook(book);
            bookService.addBook(bookToAdd, book.copiesAmount());
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/add-book") // putMapping
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
    public GetListBookDTO searchBooks(@RequestParam(value = "title") Optional<String> title,
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
            List<String> genres = Arrays.stream(genre.get().split(","))
                    .map(String::trim) // обрезка пробелов у каждого жанра
                    .collect(Collectors.toList());
            System.out.println(genres);
            if(genres.size() > 1) {
                System.out.println("1");
                res = bookService.getBooksByGenres(genres);
            }
            else
                res = bookService.findBooksByGenreStartingWith(genre.get().trim());
        } else {
            throw new NoRequestedParametersWereProvidedException();
        }

        return bookMapper.mapToGetListBookDTO(res);
    }

    //@CrossOrigin(origins = "http://localhost:3001")
    @GetMapping("/info")
    public BooksInfoDTO viewBookInfo(@RequestParam(value = "isbn") Optional<Long> isbn, Authentication authentication) {

        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            String fullName = readerDetails.getUsername();/*.concat(readerDetails.getAuthorities().toString());//readerDetails.getReader().toString().concat("; role: ").concat(readerDetails.getAuthorities().toString());*/
            log.info("Authenticated readers name: {}", fullName);
        } else if(authentication != null && authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            String fullName = librarianDetails.getUsername();
            log.info("Authenticated librarians name: {}", fullName);
        }

        else {
            log.info("Authentication principal is not an instance of ReaderDetails or is null");
        }
        log.info("Req to /book/get-book-by-isbn?isbn={}", isbn);
        var res = isbn.flatMap(bookService::findBookByIsbn)
                .map(book -> bookMapper
                        .mapToBookInfoDTO(book,
                                bookCopyService.countAvailableBookCopies(book.getIsbn())
                        ))
                .orElseThrow(NoRequestedParametersWereProvidedException::new);
        log.info("Res: {}", res);
        return res;
    }

    @GetMapping("/novelties")
    public GetListBookDTO getNovelties(){
        var novelties = bookService.getNovelties(bookProperties.noveltiesAmount());

        if (novelties.isEmpty())
            throw new NoNoveltiesWereFoundException();
        else
            return bookMapper.mapToGetListBookDTO(novelties);

    }
}
