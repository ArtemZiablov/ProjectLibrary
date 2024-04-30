package ua.karazin.interfaces.ProjectLibrary.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.karazin.interfaces.ProjectLibrary.dto.*;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;
import ua.karazin.interfaces.ProjectLibrary.models.Translator;
import ua.karazin.interfaces.ProjectLibrary.services.AuthorService;
import ua.karazin.interfaces.ProjectLibrary.services.GenreService;
import ua.karazin.interfaces.ProjectLibrary.services.TranslatorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorService authorService;
    private final TranslatorService translatorService;
    private final GenreService genreService;


    public GetListBookDTO mapToGetListBookDTO(List<Book> searchedBooks){
        return new GetListBookDTO(searchedBooks.stream()
                .map(book -> new GetBookDTO(
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthors().stream().
                                map(Author::getFullName).collect(Collectors.joining(", ")),
                        book.getGenres().stream().
                                map(Genre::getGenreName).collect(Collectors.joining(", ")),
                        book.getBookPhoto()
                ))
                .toList()
        );
    }

    public BooksInfoDTO mapToBookInfoDTO(Book book){
        return new BooksInfoDTO(
                book.getIsbn(),
                book.getTitle(),
                book.getYearOfPublishing(),
                book.getNumberOfPages(),
                book.getAnnotation(),
                book.getLanguage(),
                book.getBookPhoto(),
                book.getAuthors().stream()
                        .map(author -> author.getFullName()
                        ).toList(),
                book.getTranslators().stream()
                        .map(translator -> translator.getFullName()
                        ).toList(),
                book.getGenres().stream()
                        .map(genre -> genre.getGenreName()
                        ).toList()
        );
    }

    public Book mapToBook(BookDTO bookToAddDTO) {
        Book book = new Book();
        book.setIsbn(bookToAddDTO.isbn());
        book.setTitle(bookToAddDTO.title());
        book.setYearOfPublishing(bookToAddDTO.yearOfPublishing());
        book.setNumberOfPages(bookToAddDTO.numberOfPages());
        book.setAnnotation(bookToAddDTO.annotation());
        book.setLanguage(bookToAddDTO.language());
        book.setBookPhoto(bookToAddDTO.bookPhoto());
        book.setAuthors(mapToAuthors(bookToAddDTO.authors()));
        book.setTranslators(mapToTranslators(bookToAddDTO.translators()));
        book.setGenres(mapToGenres(bookToAddDTO.genres()));
        return book;
    }

    protected List<Author> mapToAuthors(List<AuthorDTO> authorDTOs) {
        if (authorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return authorDTOs.stream()
                .map(authorDTO -> {
                    var optionalAuthor = authorService.findAuthorByFullNameAndDateOfBirth(
                            authorDTO.fullName(),
                            authorDTO.dateOfBirth());

                    if (optionalAuthor.isPresent()) {
                        return optionalAuthor.get();
                    } else {
                        Author author = new Author();

                        author.setFullName(authorDTO.fullName());
                        author.setNationality(authorDTO.nationality());
                        author.setDateOfBirth(authorDTO.dateOfBirth());
                        author.setBooks(new ArrayList<>());
                        authorService.AddAuthor(author);  // TODO

                        return author;
                    }
                })
                .collect(Collectors.toList());
    }

    protected List<Translator> mapToTranslators(List<TranslatorDTO> translatorDTOs) {
        if (translatorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return translatorDTOs.stream()
                .map(translatorDTO -> {
                    var optionalTranslator = translatorService.findTranslatorByFullName(translatorDTO.fullName());

                    if (optionalTranslator.isPresent()){
                        return optionalTranslator.get();
                    } else {
                        Translator translator = new Translator();

                        translator.setFullName(translatorDTO.fullName());
                        translator.setBooks(new ArrayList<>());
                        translatorService.addTranslator(translator);

                        return translator;
                    }
                })
                .collect(Collectors.toList());
    }

    protected List<Genre> mapToGenres(List<GenreDTO> genreDTOS) {
        if (genreDTOS == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return genreDTOS.stream()
                .map(genreDTO -> {
                    var optionalGenre = genreService.findGenreByName(genreDTO.genreName());

                    if (optionalGenre.isPresent()) {
                        return optionalGenre.get();
                    } else {
                        Genre genre = new Genre();

                        genre.setGenreName(genreDTO.genreName());
                        genre.setBooks(new ArrayList<>());
                        genreService.addGenre(genre);

                        return genre;
                    }
                })
                .collect(Collectors.toList());
    }


}
