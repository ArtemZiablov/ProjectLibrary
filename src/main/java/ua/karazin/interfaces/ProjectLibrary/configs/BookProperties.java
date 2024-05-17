package ua.karazin.interfaces.ProjectLibrary.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("book")
public record BookProperties(
        int bookAssignAmount,
        int bookReserveAmount,
        int reservationDays,
        int noveltiesAmount,
        String activeReservationStatus,
        String awaitReservationStatus

) {
}
