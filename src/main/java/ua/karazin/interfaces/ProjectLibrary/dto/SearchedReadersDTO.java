package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.List;

public record SearchedReadersDTO(
        List<SearchReaderDTO> searchedReaders
) {
}
