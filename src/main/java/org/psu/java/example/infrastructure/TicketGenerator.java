package org.psu.java.example.infrastructure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.domain.Ticket;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.IntStream;

public interface TicketGenerator {
    static TicketGenerator getInstance(GeneratorType type) {
        return switch (type) {
            case FOUR -> new BaseGenerator(4);
            case SIX -> new RecordTicketGenerator();
            case EIGHT -> new EightDigitsTicketGenerator();
            default -> throw new IllegalArgumentException();
        };
    }
    Iterator<Ticket> getTickets();
    Optional<Ticket> getTicket(int number);
}

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
abstract class AbstractGenerator implements TicketGenerator {
    @Getter(AccessLevel.PROTECTED)
    int length;

    protected abstract Ticket toTicket(int number);

    protected IntStream getNumbersAsStream() {
        var maxNumber = (int) Math.pow(10, length);
        return IntStream.rangeClosed(0, maxNumber);
    }

    @Override
    public Iterator<Ticket> getTickets() {
        return getNumbersAsStream().mapToObj(this::toTicket).iterator();
    }

    @Override
    public Optional<Ticket> getTicket(int number) {
        var maxNumber = Math.pow(10, length);
        return Optional
                .of(number)
                .filter(n -> number >= 0 && number < maxNumber)
                .map(this::toTicket);
    }
}

/**
 * Базовая реализация генератора
 */
class BaseGenerator extends AbstractGenerator {

    public BaseGenerator(int length) {
        super(length);
    }

    @Override
    protected Ticket toTicket(int number) {
        return new TicketRecordImpl(getLength(), number);
    }
}

@Service
class EightDigitsTicketGenerator extends AbstractGenerator {

    public EightDigitsTicketGenerator() {
        super(8);
    }

    @Override
    protected Ticket toTicket(int number) {
        return  (TheTicket) () -> number;
    }

    interface TheTicket extends Ticket {
        @Override
        default int getLength() {
            return 8;
        }
    }
}
class SixDigitsTicketGenerator implements TicketGenerator {

    @Override
    public Iterator<Ticket> getTickets() {
        return IntStream
                .range(0, 1000000)
                .mapToObj(number -> new TicketImpl(6, number))
                .map(Ticket.class::cast)
                .iterator();
    }

    @Override
    public Optional<Ticket> getTicket(final int number) {
        return Optional
                .of(number)
                .filter(n -> number >= 0 && number < 1000000)
                .map(n -> new TicketImpl(6, n));
    }
}

@Service
@Primary
@Scope("prototype")
class RecordTicketGenerator extends AbstractGenerator {

    public RecordTicketGenerator() {
        super(6);
    }

    @Override
    protected Ticket toTicket(int number) {
        return new TicketRecordImpl(getLength(), number);
    }
}

class LambdaTicketGenerator extends AbstractGenerator {

    public LambdaTicketGenerator() {
        super(6);
    }

    @Override
    protected Ticket toTicket(int number) {
       return (TheTicket) () -> number;
    }

    interface TheTicket extends Ticket {
        @Override
        default int getLength() {
            return 6;
        }
    }
}


