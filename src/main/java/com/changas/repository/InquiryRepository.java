package com.changas.repository;

import com.changas.model.Inquiry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InquiryRepository extends CrudRepository<Inquiry, Long> {
    List<Inquiry> findByChangaId(Long changaId);
}
