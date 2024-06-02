package com.changas.repository;

import com.changas.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.changa.id = :changaId AND r.reviewer.id = :customerId")
    Optional<Review> findByChangaIdAndReviewerId(@Param("changaId") Long changaId, @Param("customerId") Long customerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.changa.id = :changaId")
    Optional<Double> getAverageRateForChanga(@Param("changaId") Long changaId);

    @Query("SELECT COUNT(r.rating) FROM Review r WHERE r.changa.id = :changaId")
    Optional<Integer> getRateAmountForChanga(@Param("changaId") Long changaId);

    @Query("SELECT r FROM Review r WHERE r.changa.id = :changaId")
    List<Review> getReviewsByChanga(@Param("changaId") Long changaId);
}
