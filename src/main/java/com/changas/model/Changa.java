package com.changas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Changa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    @ElementCollection
    private List<String> topics;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer provider;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Notification> changaNotifications;


    public void addHireChangaNotification(Notification notification) {
        this.changaNotifications.add(notification);
    }
}
