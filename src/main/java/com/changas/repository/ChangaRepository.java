package com.changas.repository;

import com.changas.model.Changa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangaRepository extends CrudRepository<Changa, Long>{

    List<Changa> findByTitleContainingIgnoreCase(String title);

    List<Changa> findByTitleLike(String title);
}
