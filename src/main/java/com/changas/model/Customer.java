package com.changas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    private Set<Changa> posts = new HashSet<>();

    public void saveChangaPost(Changa changa) {
        this.posts.add(changa);
    }
}
