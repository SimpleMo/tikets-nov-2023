package org.psu.java.example.presentation.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.infrastructure.GeneratorType;

import java.util.Collection;

/**
 * Класс для хранения типа запроса к REST API
 */
@Data
@Entity
@Builder
@Table(name = "RESPONSE_TYPE")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseType {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "GENERATOR_TYPE")
    GeneratorType type;

    @Column(name = "MULTIPLICITY")
    Integer multiplicity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    Collection<ResponseHistory> responseHistories;

}
