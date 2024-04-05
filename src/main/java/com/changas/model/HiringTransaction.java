package com.changas.model;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HiringTransaction {
    @DocumentId
    private String id;
    private String changaId;
    private String customerId;
    private Instant creationDate;


}
