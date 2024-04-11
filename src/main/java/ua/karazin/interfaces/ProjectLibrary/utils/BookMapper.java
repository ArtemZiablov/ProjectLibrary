package ua.karazin.interfaces.ProjectLibrary.utils;

import org.springframework.stereotype.Component;
import ua.karazin.interfaces.ProjectLibrary.dto.AuthorDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.BookToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.GenreDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.TranslatorDTO;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;
import ua.karazin.interfaces.ProjectLibrary.models.Translator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book mapToBook(BookToAddDTO bookToAddDTO) {
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
                    Author author = new Author();
                    // Assuming you have methods to set properties of Author from AuthorDTO
                    //author.setName(authorDTO.getName());
                    // Set other properties
                    author.setFullName(authorDTO.fullName());
                    author.setNationality(authorDTO.nationality());
                    author.setDateOfBirth(authorDTO.dateOfBirth());

                    return author;
                })
                .collect(Collectors.toList());
    }

    protected List<Translator> mapToTranslators(List<TranslatorDTO> translatorDTOs) {
        if (translatorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return translatorDTOs.stream()
                .map(translatorDTO -> {
                    Translator translator = new Translator();
                    // Assuming you have methods to set properties of Translator from TranslatorDTO
                    //translator.setName(translatorDTO.getName());
                    // Set other properties
                    return translator;
                })
                .collect(Collectors.toList());
    }

    protected List<Genre> mapToGenres(List<GenreDTO> authorDTOs) {
        if (authorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return authorDTOs.stream()
                .map(authorDTO -> {
                    Genre genre = new Genre();
                    // Assuming you have methods to set properties of Author from AuthorDTO
                    //author.setName(authorDTO.getName());
                    // Set other properties
                    return genre;
                })
                .collect(Collectors.toList());
    }
}
