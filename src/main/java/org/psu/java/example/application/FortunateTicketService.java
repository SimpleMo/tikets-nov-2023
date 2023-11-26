package org.psu.java.example.application;

import org.psu.java.example.domain.Ticket;

import java.util.Iterator;

public interface FortunateTicketService {
    static FortunateTicketService getInstance() {
        return new FortunateTicketImpl();
    }
    static FortunateTicketService getStreamInstance() {
        return new FortunateTicketStreamImpl(EvenDecorator::new);
    }

    default int count(Iterator<Ticket> tickets) {
        var result = 0;
        while (tickets.hasNext()) {
            var ticket = tickets.next();
            if (ticket.isFortunate()) {
                result++;
            }
        }
        return result;
    }
}

class FortunateTicketImpl implements FortunateTicketService {
}

