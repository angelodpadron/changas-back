package com.changas.model;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Changa {
    @DocumentId
    public String id;
    public String title;
    public String description;
    public String photoUrl;
    public List<String> topics;
    public Customer provider;
}
