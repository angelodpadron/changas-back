package com.changas.config;

import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.CustomerSummary;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataLoader {
    private final Firestore firestore;
    private final String CHANGAS_COLLECTION = "changas";
    private final String CUSTOMERS_COLLECTION = "customers";
    private final String HIRINGS_COLLECTION = "hiringTransactions";

    @SneakyThrows
    @PostConstruct
    public void load() {
        log.warn("Clearing previous collection and adding entries to Firestore emulator...");

        this.clearCollections(List.of(CHANGAS_COLLECTION, CUSTOMERS_COLLECTION, HIRINGS_COLLECTION));
        List<CustomerSummary> customerSummaries = this.addCustomers(CUSTOMERS_COLLECTION);
        this.addChangasAs(customerSummaries, CHANGAS_COLLECTION);

        log.info("Added entries to Firestore emulator");
    }

    private void addChangasAs(List<CustomerSummary> customerSummaries, String changasCollection) {

        List<Changa> changas = List.of(
                Changa.builder()
                        .title("Servicio de poda")
                        .description("Elit laboris cillum et labore sunt pariatur sunt duis.")
                        .photoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Knotwilgen_knotten.jpg/800px-Knotwilgen_knotten.jpg")
                        .topics(List.of("Jardin", "Exterior", "Hogar"))
                        .build(),
                Changa.builder()
                        .title("Trabajos de albañileria")
                        .description("Deserunt minim dolor velit adipisicing aliqua eiusmod duis non qui.")
                        .photoUrl("https://stockmansarquitectura.files.wordpress.com/2013/09/foto0159.jpg")
                        .build()
        );

        for (int i = 0; i < changas.size() && i < customerSummaries.size(); i++) {
            Changa changa = changas.get(i);
            changa.providerSummary = customerSummaries.get(i);
        }

        changas.forEach(changa -> this.addToCollection(changa, changasCollection));
    }

    private List<CustomerSummary> addCustomers(String customersCollection) {
        List<Customer> customers = List.of(
                Customer.builder().name("Pepe").email("pepe@example.com").photoUrl("photo_url_here").build(),
                Customer.builder().name("Riquelme").email("riquelme@example.com").photoUrl("photo_url_here").build()
        );

        List<CustomerSummary> customerSummaries = new ArrayList<>();

        for (Customer customer : customers) {
            String id = addToCollection(customer, customersCollection);
            customerSummaries.add(
                    CustomerSummary.builder()
                            .id(id)
                            .name(customer.getName())
                            .email(customer.getEmail())
                            .photoUrl(customer.getPhotoUrl())
                            .build());
        }

        return customerSummaries;
    }

    @SneakyThrows
    private <T> String addToCollection(T doc, String collectionName) {

        DocumentReference docRef = firestore.collection(collectionName).document();
        ApiFuture<WriteResult> resultFuture = docRef.set(doc);

        try {
            resultFuture.get();
            return docRef.getId();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new Exception("Write operation was interrupted.", e);
        } catch (ExecutionException e) {
            throw new Exception("Write operation failed to execute.", e.getCause());
        }
    }

    private void clearCollections(List<String> collectionNames) throws InterruptedException, ExecutionException {

        for (String collectionName : collectionNames) {
            CollectionReference collection = firestore.collection(collectionName);
            ApiFuture<QuerySnapshot> future = collection.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete().get();
            }
        }


    }
}
