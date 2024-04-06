package com.changas.service;

import com.changas.dto.HiringOverviewDTO;
import com.changas.exceptions.ChangaNotFoundException;
import com.changas.exceptions.CustomerNotFoundException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final Firestore firestore;
    private final ChangaService changaService;
    private final String CUSTOMERS_COLLECTION = "customers";
    private final String HIRINGS_COLLECTION = "hiringTransactions";


    // TODO: Improve exception handling and asynchronous handling
    public List<HiringOverviewDTO> getHiringsFromCustomer(String customerId) throws ExecutionException, InterruptedException, CustomerNotFoundException, ChangaNotFoundException {

        DocumentReference customerRef = firestore.collection(CUSTOMERS_COLLECTION).document(customerId);
        ApiFuture<DocumentSnapshot> future = customerRef.get();
        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            throw new CustomerNotFoundException("No customer found with id " + customerId);
        }

        List<String> hiringIds = document.toObject(Customer.class).getHirings();
        List<HiringOverviewDTO> hiringOverviews = new ArrayList<>();

        for (String hiringId : hiringIds) {
            DocumentReference transactionRef = firestore.collection(HIRINGS_COLLECTION).document(hiringId);
            ApiFuture<DocumentSnapshot> transactionFuture = transactionRef.get();
            DocumentSnapshot transactionDoc = transactionFuture.get();

            if (transactionDoc.exists()) {
                HiringTransaction hiringTransaction = transactionDoc.toObject(HiringTransaction.class);

                Changa changa = changaService.getChangaById(hiringTransaction.getChangaId());

                hiringOverviews.add(
                        HiringOverviewDTO.builder()
                                .hiringId(hiringId)
                                .changaId(changa.getId())
                                .changaDescription(changa.getDescription())
                                .changaTitle(changa.getTitle())
                                .changaPhotoUrl(changa.getPhotoUrl())
                                .creationDate(hiringTransaction.getCreationDate())
                                .build());
            }

        }

        return hiringOverviews;

    }

}
