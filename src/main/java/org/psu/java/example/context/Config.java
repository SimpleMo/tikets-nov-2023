package org.psu.java.example.context;

import org.psu.java.example.application.EvenDecorator;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.application.FortunateTicketStreamImpl;
import org.psu.java.example.application.MultipleOfFiveDecorator;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.infrastructure.GeneratorType;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.psu.java.example.infrastructure.TicketImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.UnaryOperator;

/**
 * Конфигурация
 */
@Configuration
@ComponentScan(basePackageClasses = {TicketImpl.class, FortunateTicketService.class})
public class Config {
    @Bean("evenFortunateTicketService")
    FortunateTicketService getEvenFortunateTicketService(@Qualifier("evenDecorator") UnaryOperator<Ticket> decorator) {
        return new FortunateTicketStreamImpl(decorator);
    }
    @Bean("multipleOfFiveFortunateTicketService")
    FortunateTicketService getMultipleOfFiveFortunateTicketService(@Qualifier("multipleOfFiveDecorator") UnaryOperator<Ticket> decorator) {
        return new FortunateTicketStreamImpl(decorator);
    }

    @Bean("fourDigitsTicketGenerator")
    TicketGenerator getFourDigitsTicketGenerator() {
        return TicketGenerator.getInstance(GeneratorType.FOUR);
    }

    @Bean("evenDecorator")
    UnaryOperator<Ticket> getEvenDecorator() {
        return EvenDecorator::new;
    }

    @Bean("multipleOfFiveDecorator")
    UnaryOperator<Ticket> getMultipleOfFiveDecorator() {
        return MultipleOfFiveDecorator::new;
    }

}
