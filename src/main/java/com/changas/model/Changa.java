package com.changas.model;

import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import jakarta.persistence.*;
import lombok.*;

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
    private Boolean available;
    @OneToOne(cascade = CascadeType.ALL)
    private ServiceArea serviceArea;

    public Changa(String title, String description, String photoUrl, Set<String> topics, Customer provider, ServiceArea serviceArea) {
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
        this.topics = topics;
        this.provider = provider;
        this.available = true;
        this.serviceArea = serviceArea;
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
