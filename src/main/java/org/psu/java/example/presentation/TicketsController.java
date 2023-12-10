package org.psu.java.example.presentation;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.GeneratorType;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.psu.java.example.presentation.entities.ResponseHistory;
import org.psu.java.example.presentation.entities.ResponseHistoryRepository;
import org.psu.java.example.presentation.entities.ResponseType;
import org.psu.java.example.presentation.entities.ResponseTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * REST-контроллер для работы с билетами
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketsController {

    TicketGenerator sixDigitsTicketGenerator;
    TicketGenerator fourDigitsTicketGenerator;
    TicketGenerator eightDigitsTicketGenerator;
    FortunateTicketService fortunateTicketService;
    FortunateTicketService evenFortunateTicketService;
    FortunateTicketService multipleOfFiveFortunateTicketService;
    ResponseHistoryRepository historyRepository;
    ResponseTypeRepository responseTypeRepository;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    Map<Integer, FortunateTicketService> fortunateTicketServices = prepare();

    private Map<Integer, FortunateTicketService> prepare() {
        Map<Integer, FortunateTicketService> result = new HashMap<>();
        result.put(2, evenFortunateTicketService);
        result.put(5, multipleOfFiveFortunateTicketService);
        return result;
    }

    @GetMapping("/six")
    public ResponseEntity<Integer> getSixDigitsFortunateTicketsCount() {
        // Servlet(HttpRequest, HttpResponse) doGet, doPost, doPut Jackson
        int count = fortunateTicketService.count(sixDigitsTicketGenerator.getTickets());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/four")
    public ResponseEntity<Integer> getFourDigitsFortunateTicketsCount() {
        int count = fortunateTicketService.count(fourDigitsTicketGenerator.getTickets());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/extra/{generatorType}")
    public ResponseEntity<Integer> getPathVariableFortunateTicketsCount(@PathVariable GeneratorType generatorType) {
        return countWithGenerator(fortunateTicketService, generatorType);
    }

    @GetMapping("/extra")
    public ResponseEntity<Integer> getRequestParamFortunateTicketsCount(@RequestParam(name = "type", required = false) GeneratorType generatorType) {
        if (generatorType == null) {
            return ResponseEntity.badRequest().build();
        }
        return countWithGenerator(fortunateTicketService, generatorType);
    }

    @GetMapping("/extra-multiplicity/{type}")
    public ResponseEntity<Integer> getFortunateTicketsMultiplicityCount(@PathVariable(name = "type") GeneratorType generatorType,
                                                                        @RequestParam int multiplicity) {
        return switch (multiplicity) {
            case 2 -> countWithGenerator(evenFortunateTicketService, generatorType);
            case 5 -> countWithGenerator(multipleOfFiveFortunateTicketService, generatorType);
            default -> ResponseEntity.badRequest().build();
        };
    }

    @PostMapping
    public ResponseEntity<Collection<FortunateTicketResponse>> getFortunateTicketCounts(@RequestBody Collection<FortunateTicketRequest> body) {
        var counts = body.stream().map(this::calculate).toList();
        return ResponseEntity.ok(counts);
    }

    private FortunateTicketResponse calculate(FortunateTicketRequest item) {
        var responseType =
                responseTypeRepository
                        .findByTypeAndMultiplicity(item.type(), item.multiplicity())
                        .orElseGet(() -> ResponseType.builder().type(item.type()).multiplicity(item.multiplicity()).responseHistories(new ArrayList<>()).build());

        var historyBuilder = ResponseHistory.builder();
        historyBuilder.startTime(LocalDateTime.now());

        if (item.multiplicity() == null) {
            var count = countWithGenerator(fortunateTicketService, item.type());
            historyBuilder.result(count.getBody()).endTime(LocalDateTime.now());
            responseType.getResponseHistories().add(historyBuilder.build());

            responseTypeRepository.save(responseType);

            return new FortunateTicketResponse(item.type(), item.multiplicity(), count.getBody());
        }

        var service = getFortunateTicketServices().getOrDefault(item.multiplicity(), fortunateTicketService);
        var count = countWithGenerator(service, item.type());

        historyBuilder.result(count.getBody()).endTime(LocalDateTime.now());
        var saved = responseTypeRepository.save(responseType);
        historyBuilder.responseType(saved);

//        responseType.getResponseHistories().add(historyBuilder.build());
        var history = historyBuilder.build();
        historyRepository.save(history);

        return new FortunateTicketResponse(item.type(), item.multiplicity(), count.getBody());
    }

    private ResponseEntity<Integer> countWithGenerator(FortunateTicketService service, GeneratorType generatorType) {
        return switch (generatorType) {
            case FOUR -> {
                int count = service.count(fourDigitsTicketGenerator.getTickets());
                yield ResponseEntity.ok(count);
            }
            case SIX -> {
                int count = service.count(sixDigitsTicketGenerator.getTickets());
                yield ResponseEntity.ok(count);
            }
            case EIGHT -> {
                int count = service.count(eightDigitsTicketGenerator.getTickets());
                yield ResponseEntity.ok(count);
            }
        };
    }

    record FortunateTicketRequest(GeneratorType type, Integer multiplicity) { }
    record FortunateTicketResponse(GeneratorType type, Integer multiplicity, int count) { }

}
