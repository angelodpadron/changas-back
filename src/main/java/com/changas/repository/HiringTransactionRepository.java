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
    @Query("SELECT t FROM HiringTransaction t WHERE (t.changa.provider.id = :customer_id OR t.requester.id = :customer_id)  AND t.status = :status")
    Set<HiringTransaction> findByCustomerIdAndStatus(@Param("customer_id") Long id, @Param("status") TransactionStatus status);

    @EntityGraph("transaction-with-full-details")
    @Query("SELECT t FROM HiringTransaction t WHERE t.changa.provider.id = :party_id OR t.requester.id =:party_id")
    Set<HiringTransaction> allTransactionsFromCustomer(@Param("party_id") Long id);

    @EntityGraph("transaction-with-full-details")
    @Query("SELECT t FROM HiringTransaction t WHERE t.id = :transaction_id AND (t.changa.provider.id = :customer_id OR t.requester.id = :customer_id)")
    Optional<HiringTransaction> findCustomerTransactionById(@Param("transaction_id") Long transactionId, @Param("customer_id") Long customerId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END " +
            "FROM HiringTransaction t " +
            "WHERE t.requester.id = :requester_id " +
            "AND t.changa.id = :changa_id " +
            "AND t.status = :status")
    boolean hasByRequesterAndChangaAndStatus(@Param("requester_id") Long customerId, @Param("changa_id") Long changaId, @Param("status") TransactionStatus status);
}
