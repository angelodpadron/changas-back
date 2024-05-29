package com.changas.model;

import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class Changa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    @ElementCollection
    private Set<String> topics;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer provider;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;
    private Boolean available;

    public Changa(String title, String description, String photoUrl, Set<String> topics, Customer provider) {
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
        this.topics = topics;
        this.provider = provider;
        this.reviews = new ArrayList<>();
        this.available = true;
    }

    public void deactivateAs(Customer customer) throws UnauthorizedChangaEditException {
        checkIfCanDeactivate(customer);
        this.available = false;

    }

    private void checkIfCanDeactivate(Customer customer) throws UnauthorizedChangaEditException {
        if (!customer.getId().equals(this.provider.getId())) {
            throw new UnauthorizedChangaEditException();
        }
    }
}
