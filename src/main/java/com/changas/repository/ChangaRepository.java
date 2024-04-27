package com.changas.repository;

import com.changas.model.Changa;
import com.changas.model.Notification;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Configuration
@Repository
public interface ChangaRepository extends CrudRepository<Changa, Long>{

    List<Changa> findByTitleContainingIgnoreCase(String title);

    List<Changa> findByTitleLike(String title);

    //@Query(value = "SELECT c FROM changa c inner join c.notifications n WHERE n.isRead = False")
    List<Notification> findNotificationsById(Long changaid);
}
