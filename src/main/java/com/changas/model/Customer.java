package com.changas.model;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    @DocumentId
    private String id;
    private String name;
    private String email;
    private String photoUrl;
    private List<String> hirings;
}
