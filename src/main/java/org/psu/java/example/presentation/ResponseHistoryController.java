package org.psu.java.example.presentation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.presentation.entities.ResponseHistory;
import org.psu.java.example.presentation.entities.ResponseHistoryRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для работы с сохранённой статистикой
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/response-history")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResponseHistoryController {
    ResponseHistoryRepository repository;

    @GetMapping
    public Iterable<ResponseHistory> findAll() {
        return repository.findAll();
    }

    @GetMapping("/by-start-time/{startTime}")
    public List<ResponseHistory> findByStartTime(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        return repository.findAllByStartTimeAfter(startTime);
    }
    @GetMapping("/result-by-start-time/{startTime}")
    public List<Integer> findResultsByStartTime(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        return repository.getResultsByStartTime(startTime);
    }

    @GetMapping("/by-result/{result}")
    public List<ResponseHistory> findByResult(@PathVariable Integer result) {
        return repository.findByResult(result);
    }
}
