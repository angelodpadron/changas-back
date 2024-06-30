package com.changas.repository;

import com.changas.model.Inquiry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends CrudRepository<Inquiry, Long> {
    List<Inquiry> findByChangaId(Long changaId);

    @Query("SELECT i FROM Inquiry i WHERE i.changa.provider.id = :customerId AND i.answer IS NULL")
    List<Inquiry> getPendingInquiriesFor(@Param("customerId") Long customerId);

    @Query("SELECT i FROM Inquiry i WHERE i.customer.id = :customerId AND i.answer IS NOT NULL AND i.read = FALSE")
    List<Inquiry> getUnreadAnswersFor(@Param("customerId") Long customerId);
}
