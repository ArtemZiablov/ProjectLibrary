package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.*;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoRequestedParametersWereProvidedException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NotAuthenticatedException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReaderNotExistException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReadersNotFoundException;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j(topic = "ReaderController")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reader")
@RestController
public class ReaderController {
    private final ReaderService readerService;
    private final BookCopyService bookCopyService;
    private final BookReservationService bookReservationService;

    @GetMapping("/info")
    public ReadersInfoDTO getReadersInfo(@RequestParam("id") Integer id, Authentication authentication) {

        log.info("req to /reader/info?id={}", id);
        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            int readerId = readerDetails.reader().getId();
            if (readerId == id) {
                return getInfo(id);
            } else throw new NotAuthenticatedException();
        } else if (authentication != null && authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            return getInfo(id);
        }

        throw new NotAuthenticatedException();
    }

    private ReadersInfoDTO getInfo(Integer id) {
        return readerService.findReaderById(id).map(reader ->
                new ReadersInfoDTO(
                        reader.getId(),
                        reader.getFullName(),
                        reader.getDateOfBirth(),
                        reader.getPhoneNumber(),
                        reader.getEmail(),
                        reader.isDebtor(),
                        reader.getProfilePhoto(),
                        bookCopyService.getReadersBookCopies(id),
                        bookReservationService.findReadersReservedBooks(id).stream()
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

                )
        ).orElseThrow(ReaderNotExistException::new);
    }


    @GetMapping("/search")
    public SearchedReadersDTO findReaders(@RequestParam("name") Optional<String> name,
                                          @RequestParam("id") Optional<Integer> id,
                                          @RequestParam("phoneNum") Optional<String> phoneNum) {

        Optional<List<Reader>> res;
        if (name.isPresent() && name.get().trim().length() >= 4) {
            log.info("Req to /reader/search?name={}", name);
            res = readerService.findReadersByFullNameStartingWith(name.get());
        } else if (id.isPresent()) {
            log.info("Req to /reader/search?id={}", id);
            res = readerService.findReadersById(id.get());
        } else if (phoneNum.isPresent()) {
            log.info("Req to /reader/search?phoneNum={}", phoneNum);
            res = readerService.findReadersByPhoneNumber(phoneNum.get());
        } else {
            throw new ReadersNotFoundException();
        }

        return mapToSearchedReadersDTO(res);
    }

    @GetMapping("taken-books")
    public List<ReadersBookCopyDTO> getReadersTakenBooks(@RequestParam("readerId") Optional<Integer> readerId) {
        return bookCopyService.getReadersBookCopies(readerId.orElseThrow(NoRequestedParametersWereProvidedException::new));
    }

    @GetMapping("reserved-books")
    public List<GetBookDTO> getReadersReservedBooks(@RequestParam("readerId") Optional<Integer> readerId) {
        return bookReservationService.findReadersReservedBooks(readerId.orElseThrow(NoRequestedParametersWereProvidedException::new)).stream().map(book -> new GetBookDTO(
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthors().stream().
                                map(Author::getFullName).collect(Collectors.joining(", ")),
                        book.getGenres().stream().
                                map(Genre::getGenreName).collect(Collectors.joining(", ")),
                        book.getBookPhoto()
                ))
                .toList();
    }


    @GetMapping("photo")
    public Map<String, String> getPhoto(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            int readerId = readerDetails.reader().getId();
            Map<String, String> res = new HashMap<>();
            String photo = readerService.getReadersPhoto(readerId);
            res.put("photo", photo);
            log.info("res: {}", res);
            return res;
        } else throw new NotAuthenticatedException();
    }

    protected SearchedReadersDTO mapToSearchedReadersDTO(Optional<List<Reader>> optionalReaders) {
        return optionalReaders.map(readers -> readers.stream()
                        .map(reader -> new SearchReaderDTO(
                                reader.getId(), reader.getFullName(), reader.getPhoneNumber(), reader.getProfilePhoto()
                        )).toList())
                .map(SearchedReadersDTO::new)
                .orElseThrow(ReadersNotFoundException::new);
    }
}
