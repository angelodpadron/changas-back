package com.changas.service;

import com.changas.model.Changa;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChangaService {
    private final Firestore firestore;

    public List<Changa> getAllChangas() throws InterruptedException, ExecutionException {
        List<Changa> changas = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = firestore.collection("changas").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            changas.add(document.toObject(Changa.class));
        }

        return changas;
    }
}
