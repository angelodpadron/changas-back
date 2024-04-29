package com.changas.repository;

import com.changas.model.Changa;
import com.changas.model.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChangaRepository extends CrudRepository<Changa, Long> {
    @Query("SELECT c FROM Changa c JOIN c.topics t WHERE LOWER(t) IN :topics")
    Set<Changa> findChangasByTopics(@Param("topics") Set<String> topics);

    Set<Changa> findByTitleContainingIgnoreCase(String title);

    @Query(value = "SELECT n FROM Changa c JOIN c.changaNotifications n WHERE c.id = :changaId ")
    List<Notification> findNotificationsById(@Param ("changaId") Long changaId);

   @Query("SELECT c FROM Changa c JOIN c.topics t WHERE LOWER(c.title) LIKE LOWER(:title) AND LOWER(t) IN :topics")
    Set<Changa> findByTitleAndTopics(@Param("title") String title, @Param("topics") Set<String> topics);
}
