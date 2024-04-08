package com.changas.repository;

import com.changas.model.Changa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangaRepository extends CrudRepository<Changa, Long> {
}
