package com.changas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Changa changa;
    @ManyToOne
    private Customer reviewer;
    private int rating;
    private String comment;
    private String photoUrl;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    public static Review generateReview(Changa changa, Customer reviewer, int rating, String comment, String photoUrl) {
        return Review
                .builder()
                .changa(changa)
                .reviewer(reviewer)
                .rating(rating)
                .comment(comment)
                .photoUrl(photoUrl)
                .build();
    }


}
