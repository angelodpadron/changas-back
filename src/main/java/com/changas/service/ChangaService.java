package com.changas.service;

import com.changas.exceptions.ChangaNotFoundException;
import com.changas.exceptions.CustomerNotFoundException;
import com.changas.model.Changa;
import com.changas.model.HiringTransaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChangaService {
    private final Firestore firestore;

    private final String CHANGAS_COLLECTION = "changas";
    private final String CUSTOMERS_COLLECTION = "customers";
    private final String HIRINGS_COLLECTION = "hiringTransactions";

    public List<Changa> getAllChangas() throws InterruptedException, ExecutionException {
        List<Changa> changas = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = firestore.collection(CHANGAS_COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            changas.add(document.toObject(Changa.class));
        }

        return changas;
    }

    public Changa getChangaById(String changaId) throws ExecutionException, InterruptedException, ChangaNotFoundException {
        DocumentReference changaRef = firestore.collection(CHANGAS_COLLECTION).document(changaId);
        ApiFuture<DocumentSnapshot> changaFuture = changaRef.get();
        DocumentSnapshot changaSnapshot = changaFuture.get();

        if (!changaSnapshot.exists()) {
            throw new ChangaNotFoundException("No changa found with id " + changaId);
        }

        return changaSnapshot.toObject(Changa.class);
    }

    // TODO: Improve exception handling, asynchronous handling, and edge cases handling like customers hiring their own changa
    public void hireChanga(String changaId, String customerId) throws CustomerNotFoundException, ChangaNotFoundException, ExecutionException, InterruptedException {

        // Validate both the customer and the changa exist
        DocumentReference changaRef = firestore.collection(CHANGAS_COLLECTION).document(changaId);
        DocumentReference customerRef = firestore.collection(CUSTOMERS_COLLECTION).document(customerId);

        ApiFuture<DocumentSnapshot> changaFuture = changaRef.get();
        ApiFuture<DocumentSnapshot> customerFuture = customerRef.get();

        DocumentSnapshot changaSnapshot = changaFuture.get();
        DocumentSnapshot customerSnapshot = customerFuture.get();

        if (!changaSnapshot.exists()) {
            throw new ChangaNotFoundException("No changa found with id " + changaId);
        }

        if (!customerSnapshot.exists()) {
            throw new CustomerNotFoundException("No customer found with id " + customerId);
        }

        // Create the transaction document
        HiringTransaction transaction = HiringTransaction
                .builder()
                .changaId(changaId)
                .customerId(customerId)
                .creationDate(Instant.now())
                .build();

        DocumentReference transactionRef = firestore
                .collection(HIRINGS_COLLECTION)
                .document();

        transaction.setId(transactionRef.getId());

        // Save the transaction and add the id to the customer's hirings field
        ApiFuture<WriteResult> transactionFuture = transactionRef.set(transaction);
        transactionFuture.get();

        ApiFuture<WriteResult> updateCustomerFuture = firestore
                .collection(CUSTOMERS_COLLECTION)
                .document(customerId)
                .update("hirings", FieldValue.arrayUnion(transaction.getId()));
        updateCustomerFuture.get();


    }
}
