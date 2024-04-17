package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.List;

public record SearchedBooksDTO(
        List<SearchBookDTO>  searchedBooks
) {
}
