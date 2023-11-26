package org.psu.java.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 * Выводит количество шестизначных счастливых билетов
 */
@Slf4j
@AllArgsConstructor
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE)
@ComponentScan({ "org.psu.java.example.context",  "org.psu.java.example.presentation"})
public class Tickets {

    ApplicationContext context;

    @Getter(lazy = true)
    final Tickets self = prepare();

    private Tickets prepare() {
        return context.getBean(Tickets.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Tickets.class);
    }

    public void run(String... args) {
        log.info("Hello world Spring Boot style!");

        getSelf().calculate("getEvenFortunateTicketService", "recordTicketGenerator");
        getSelf().calculate("getMultipleOfFiveFortunateTicketService", "recordTicketGenerator");
        getSelf().calculate("getEvenFortunateTicketService", "eightDigitsTicketGenerator");
        getSelf().calculate("getMultipleOfFiveFortunateTicketService", "eightDigitsTicketGenerator");
        getSelf().calculate("getEvenFortunateTicketService", "getFourDigitsTicketGenerator");
        getSelf().calculate("getMultipleOfFiveFortunateTicketService", "getFourDigitsTicketGenerator");

        log.info("Finish!");
    }

    private void calculate(String serviceName, String generatorName) {
        var serviceForRecordTicketGenerator = context.getBean(serviceName, FortunateTicketService.class);
        var recordTicketGenerator = context.getBean(generatorName, TicketGenerator.class);
        var recordTicketCount = serviceForRecordTicketGenerator.count(recordTicketGenerator.getTickets());
        log.info(String.valueOf(recordTicketCount));
    }
}