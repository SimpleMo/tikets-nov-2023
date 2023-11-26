package org.psu.java.example.presentation;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для работы с билетами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketsController {

    TicketGenerator ticketGenerator;
    FortunateTicketService fortunateTicketService;

    @GetMapping
    public int getFortunateTicketsCount() {
        return fortunateTicketService.count(ticketGenerator.getTickets());
    }
}
