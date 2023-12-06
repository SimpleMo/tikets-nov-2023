package org.psu.java.example.presentation.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Сущность для хранения истории запросов к REST API
 */
@Data
@Entity
@Builder
@Table(name = "HISTORY")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseHistory implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "START_TIME", nullable = false)
    LocalDateTime startTime;

    @Column(name = "END_TIME", nullable = false)
    LocalDateTime endTime;

    @Column(name = "RESULT")
    Integer result;

    @ManyToOne
    @JoinColumn(name = "RESPONSE_TYPE_ID")
    ResponseType responseType;
}
