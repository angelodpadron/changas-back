package com.changas.repository;

import com.changas.model.HiringTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiringTransactionRepository extends CrudRepository<HiringTransaction, Long> {
}
