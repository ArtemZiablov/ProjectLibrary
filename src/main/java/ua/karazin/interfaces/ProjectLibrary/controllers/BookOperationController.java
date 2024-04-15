package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.services.BookOperationService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book_operation")
public class BookOperationController {

    private final BookOperationService bookOperationService;

    // TODO add endpoints for statistics functional...
}
