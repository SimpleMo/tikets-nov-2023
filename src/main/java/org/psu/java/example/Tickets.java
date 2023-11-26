package org.psu.java.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
        assert context != null;
        return context.getBean(Tickets.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Tickets.class);
    }
}