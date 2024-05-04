package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Map;

public record StatisticsDTO(
        Map<String, Map<String, Integer>> booksStatistics,
        Map<String, Map<String, Integer>> readersStatistics
) {
}
