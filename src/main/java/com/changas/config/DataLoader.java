package com.changas.config;

import com.changas.model.Changa;
import com.changas.model.Customer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataLoader {
    private final Firestore firestore;
    private final String COLLECTION_NAME = "changas";

    @SneakyThrows
    @PostConstruct
    public void load() {

        log.warn("Clearing previous collection and adding entries to Firestore emulator...");
        this.clearCollection();
        List.of(
                        Changa.builder()
                                .title("Servicio de poda")
                                .description("Elit laboris cillum et labore sunt pariatur sunt duis.")
                                .photoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Knotwilgen_knotten.jpg/800px-Knotwilgen_knotten.jpg")
                                .topics(List.of("Jardin", "Exterior", "Hogar"))
                                .provider(
                                        Customer.builder()
                                                .name("Pepe")
                                                .email("pepe@example.com")
                                                .photoUrl("https://uchile.cl/.imaging/default/dam/imagenes/Uchile/imagenes-noticias/151158_1_loros-argentinos-01-l_L/jcr:content.jpg")
                                                .build()
                                ).build(),
                        Changa.builder()
                                .title("Trabajos de albañileria")
                                .description("Deserunt minim dolor velit adipisicing aliqua eiusmod duis non qui.")
                                .photoUrl("https://stockmansarquitectura.files.wordpress.com/2013/09/foto0159.jpg")
                                .provider(
                                        Customer.builder()
                                                .name("Riquelme")
                                                .email("riquelme@example.com")
                                                .photoUrl("https://fcb-abj-pre.s3.amazonaws.com/img/jugadors/740_riquelme.jpg")
                                                .build()
                                ).build()
                )
                .forEach(changa -> this.addToCollection(changa, COLLECTION_NAME));

        log.info("Added entries to Firestore emulator");
    }

    @SneakyThrows
    private <T> void addToCollection(T doc, String collectionName) {

        DocumentReference docRef = firestore.collection(collectionName).document();
        ApiFuture<WriteResult> resultFuture = docRef.set(doc);

        try {
            resultFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new Exception("Write operation was interrupted.", e);
        } catch (ExecutionException e) {
            throw new Exception("Write operation failed to execute.", e.getCause());
        }
    }

    private void clearCollection() throws InterruptedException, ExecutionException {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = collection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete().get();
        }
    }
}
