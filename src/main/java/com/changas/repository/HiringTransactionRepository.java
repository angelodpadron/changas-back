package com.changas.repository;

import com.changas.model.HiringTransaction;
import com.changas.model.TransactionStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface HiringTransactionRepository extends CrudRepository<HiringTransaction, Long> {
    @Query("SELECT t FROM HiringTransaction t WHERE t.provider.id = :provider_id AND t.status = :status")
    Set<HiringTransaction> findByProviderIdAndStatus(@Param("provider_id") Long id, @Param("status")TransactionStatus status);
}
