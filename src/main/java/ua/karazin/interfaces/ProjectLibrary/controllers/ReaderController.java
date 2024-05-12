package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.dto.*;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoRequestedParametersWereProvidedException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReadersNotFoundException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReaderNotExistException;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;
import ua.karazin.interfaces.ProjectLibrary.utils.BookMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j(topic = "ReaderController")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reader")
@RestController
public class ReaderController {
    private final ReaderService readerService;
    private final BookCopyService bookCopyService;
    private final BookReservationService bookReservationService;
    private final BookMapper bookMapper;

    @GetMapping("/info")
    public ReadersInfoDTO viewReadersInfo(@RequestParam("id") Integer id) {

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
                        bookMapper.mapToGetListBookDTO(bookReservationService.findReadersReservedBooks(id))

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
    public ReadersBookCopiesDTO getReadersTakenBooks(@RequestParam("readerId") Optional<Integer> readerId) {
        return bookCopyService.getReadersBookCopies(readerId.orElseThrow(NoRequestedParametersWereProvidedException::new));
    }

    @GetMapping("reserved-books")
    public GetListBookDTO getReadersReservedBooks(@RequestParam("readerId") Optional<Integer> readerId) {
        var res = bookReservationService.findReadersReservedBooks(readerId.orElseThrow(NoRequestedParametersWereProvidedException::new));
        return bookMapper.mapToGetListBookDTO(res);
    }

    @GetMapping("photo")
    public Map<String, String> getPhoto(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            int readerId = readerDetails.reader().getId();
            Map<String, String> res = new HashMap<>();
            String photo = readerService.getReadersPhoto(readerId);
            res.put("photo", photo);
            return res;
        } else throw new NoRequestedParametersWereProvidedException();
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
