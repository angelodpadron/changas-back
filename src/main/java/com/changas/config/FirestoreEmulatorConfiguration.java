package com.changas.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.Socket;

@Configuration
@Slf4j
@Profile({"dev", "test"})
public class FirestoreEmulatorConfiguration {

    @PostConstruct
    public void checkFirestoreEmulator() {
        checkEmulator("Firestore", "localhost", 9080);
        checkEmulator("Auth", "localhost", 9099);
    }

    private void checkEmulator(String emulatorName, String host, int port) {
        try (Socket ignored = new Socket(host, port)) {
            log.info("{} emulator is up and running.", emulatorName);
        } catch (IOException e) {
            log.error("{} emulator is not running at {}:{}. Start the emulator at the specified port", emulatorName, host, port);
        }
    }

}
