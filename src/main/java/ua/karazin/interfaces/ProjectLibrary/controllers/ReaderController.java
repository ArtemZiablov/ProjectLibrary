package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.karazin.interfaces.ProjectLibrary.dto.SearchReaderDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.SearchedReadersDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReadersNotFoundException;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.util.List;
import java.util.Optional;

@Slf4j(topic = "ReaderController")
@RequiredArgsConstructor
@RequestMapping("/reader")
@RestController
public class ReaderController {
    private final ReaderService readerService;

    /*@GetMapping("/viewInfo")
    public ReadersInfoDTO viewReadersInfo(@RequestParam("id") Integer id){

    }*/

    @GetMapping("/search")
    public SearchedReadersDTO findReaders(@RequestParam("name")     Optional<String> name,
                                          @RequestParam("id")       Optional<Integer> id,
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

    protected SearchedReadersDTO mapToSearchedReadersDTO(Optional<List<Reader>> optionalReaders) {
        return optionalReaders.map(readers -> readers.stream()
                        .map(reader -> new SearchReaderDTO(
                                reader.getId(), reader.getFullName(), reader.getPhoneNumber()
                        )).toList())
                .map(SearchedReadersDTO::new)
                .orElseThrow(ReadersNotFoundException::new);
    }


}
