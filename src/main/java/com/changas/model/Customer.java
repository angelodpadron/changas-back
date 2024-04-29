package com.changas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String photoUrl;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<HiringTransaction> transactions = new HashSet<>();
    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    private Set<Changa> posts = new HashSet<>();

    public void saveHiringTransaction(HiringTransaction hiringTransaction) {
        this.transactions.add(hiringTransaction);
    }

    public void saveChangaPost(Changa changa) {
        this.posts.add(changa);
    }
}
