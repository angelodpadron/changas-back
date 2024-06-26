package com.changas.repository;

import com.changas.model.Changa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChangaRepository extends CrudRepository<Changa, Long> {
//    @Query("SELECT c FROM Changa c JOIN c.topics t WHERE LOWER(t) IN :topics")
    @Query("SELECT c FROM Changa c JOIN c.topics t WHERE LOWER(t) IN :topics GROUP BY c HAVING COUNT(DISTINCT t) = :size")
    Set<Changa> findChangasByTopics(@Param("topics") Set<String> topics, @Param("size") int size);
    Set<Changa> findByTitleContainingIgnoreCase(String title);
    @Query("SELECT c FROM Changa c JOIN c.topics t WHERE LOWER(c.title) LIKE LOWER(:title) " +
           "AND LOWER(t) IN :topics " +
           "GROUP BY c HAVING COUNT(DISTINCT t) = :size")
    Set<Changa> findByTitleAndTopics(@Param("title") String title, @Param("topics") Set<String> topics, @Param("size") int size);
}
