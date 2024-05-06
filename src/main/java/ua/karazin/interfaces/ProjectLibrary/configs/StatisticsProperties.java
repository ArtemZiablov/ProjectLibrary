package ua.karazin.interfaces.ProjectLibrary.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("statistics")
public record StatisticsProperties(
        String books,
        String copies,
        String assignedBookCopies,
        String owedBooks,
        String reservedBooks,
        String registeredReaders,
        String debtors,
        String ongoingReaders,
        String readersWhoReservedBooks
        ) {
}
