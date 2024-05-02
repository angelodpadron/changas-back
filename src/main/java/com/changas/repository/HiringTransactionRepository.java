package com.changas.repository;

import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface HiringTransactionRepository extends CrudRepository<HiringTransaction, Long> {
    @EntityGraph("transaction-with-full-details")
    @Query("SELECT t FROM HiringTransaction t WHERE t.provider.id = :provider_id AND t.status = :status")
    Set<HiringTransaction> findByProviderIdAndStatus(@Param("provider_id") Long id, @Param("status")TransactionStatus status);

    @EntityGraph("transaction-with-full-details")
    @Query("SELECT t FROM HiringTransaction t WHERE t.provider.id = :party_id OR t.requester.id =:party_id")
    Set<HiringTransaction> allCustomerTransactions(@Param("party_id") Long id);

    @EntityGraph("transaction-with-full-details")
    @Query("SELECT t FROM HiringTransaction t WHERE t.id = :transaction_id AND t.provider.id = :customer_id OR t.requester.id = :customer_id")
    Optional<HiringTransaction> findCustomerTransactionById(@Param("transaction_id") Long transactionId, @Param("customer_id") Long customerId);
}
