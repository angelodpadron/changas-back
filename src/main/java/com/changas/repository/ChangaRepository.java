package com.changas.repository;

import com.changas.model.Changa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChangaRepository extends CrudRepository<Changa, Long> {
    Set<Changa> findChangasByTopics(@Param("topics") Set<String> topics);
    Set<Changa> findByTitleContainingIgnoreCase(String title);
    Set<Changa> findByTitleAndTopics(@Param("title") String title, @Param("topics") Set<String> topics);
}
