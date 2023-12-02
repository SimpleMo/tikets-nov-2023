package org.psu.java.example.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.domain.Ticket;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service("fortunateTicketService")
//@Primary
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FortunateTicketStreamImpl implements FortunateTicketService {
    UnaryOperator<Ticket> decorate;

    public FortunateTicketStreamImpl() {
        decorate = UnaryOperator.identity();
    }

    @Override
    public int count(Iterator<Ticket> tickets) {
        Iterable<Ticket> iterable = () -> tickets;
        Stream<Ticket> ticketStream =
                StreamSupport.stream(iterable.spliterator(), false);
        return (int) ticketStream.map(decorate).filter(Ticket::isFortunate).count();

    }
}
