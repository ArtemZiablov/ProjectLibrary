package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.List;

public record GetListBookDTO(
        List<GetBookDTO>  searchedBooks
) {
}
