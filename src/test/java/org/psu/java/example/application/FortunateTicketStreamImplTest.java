package org.psu.java.example.application;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.psu.java.example.infrastructure.TicketImpl;

import java.util.Iterator;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link FortunateTicketStreamImpl}
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FortunateTicketStreamImplTest {

    @Mock
    TicketGenerator ticketGenerator;
    int maxNumber;
//    @InjectMocks
    FortunateTicketService service;

    @Spy
    Ticket mockTicket = new TicketImpl(4, 0);

    @Before
    public void setUp() {
        maxNumber = (int) Math.pow(10, 4);
        when(mockTicket.isFortunate()).thenReturn(true);
        Iterator<Ticket> mockIterator = IntStream
                .range(0, maxNumber)
                .mapToObj(number -> {
                    log.info(String.format("Формируем билет №%d", number));
                    return mockTicket;
                })
                .iterator();
        when(ticketGenerator.getTickets()).thenReturn(mockIterator);
        service = new FortunateTicketStreamImpl();
    }

    @After
    public void tearDown() {
        service = null;
        ticketGenerator = null;
        maxNumber = 0;
    }

    @Test
    public void testCount() {
        // given Pivotal Software CRM Spring Cloud (Zuul, Eurica,...)
        // EJB
        // when
        int actual = service.count(ticketGenerator.getTickets());

        // then
        assertEquals("Должно быть 10 000 счастливых билетов", maxNumber, actual);
        Mockito.verify(mockTicket, atLeast(maxNumber)).isFortunate();
    }

}