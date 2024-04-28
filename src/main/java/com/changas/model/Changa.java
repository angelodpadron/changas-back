package com.changas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

@SequenceGenerator(
        name = "customSecuence",
        sequenceName = "custom secuence",
        initialValue = 5,
        allocationSize = 1
)
public class Changa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customSecuence")
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    @ElementCollection
    private Set<String> topics;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer provider;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "changa")
    private List<Notification> changaNotifications;


    public void addHireChangaNotification(Notification notification) {
        this.changaNotifications.add(notification);
    }
}
