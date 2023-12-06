package org.psu.java.example.presentation.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для {@link ResponseHistory}
 */
@Repository
public interface ResponseHistoryRepository extends CrudRepository<ResponseHistory, Long> {
    List<ResponseHistory> findAllByStartTimeAfter(LocalDateTime startTime);

    @Query("select history from ResponseHistory history where history.result >= :result")
    List<ResponseHistory> findByResult(@Param("result") Integer result);

    @Query(value = """
            select history.RESULT
              from HISTORY history
             where history.START_TIME >= :startTime""",
            nativeQuery = true)
    List<Integer> getResultsByStartTime(@Param("startTime") LocalDateTime startTime);
}
