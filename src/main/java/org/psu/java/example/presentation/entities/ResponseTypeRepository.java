package org.psu.java.example.presentation.entities;

import org.psu.java.example.infrastructure.GeneratorType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для {@link ResponseType}
 */
@Repository
public interface ResponseTypeRepository extends CrudRepository<ResponseType, Long> {
    Optional<ResponseType> findByTypeAndMultiplicity(GeneratorType type, Integer multiplicity);

}
