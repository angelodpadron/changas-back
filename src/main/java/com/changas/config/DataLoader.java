package com.changas.config;

import com.changas.dto.area.ServiceAreaRequest;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.WorkAreaDetailsDTO;
import com.changas.service.AuthService;
import com.changas.service.ChangaService;
import com.changas.service.HiringTransactionService;
import com.changas.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final AuthService authService;
    private final ChangaService changaService;
    private final HiringTransactionService transactionService;
    private final InquiryService inquiryService;

    @Override
    public void run(String... args) throws Exception {

        log.info("Adding filler data into DB");

        String basePhotoUrl = "https://placehold.co/600x400?text=";
        authService.signup(new SignupRequest("Pepe", basePhotoUrl + "Pepe", "pepe@email.com", "password"));
        authService.signup(new SignupRequest("Sofia", basePhotoUrl + "Sofia", "sofia@email.com", "password"));
        authService.signup(new SignupRequest("Matias", basePhotoUrl + "Matias", "matias@email.com", "password"));

        authService.login(new LoginRequest("pepe@email.com", "password"));
        ServiceAreaRequest serviceAreaRequest = new ServiceAreaRequest("Direccion", new double[]{-58.2912458, -34.7955703});
        ChangaOverviewDTO changa1 = changaService.createChanga(new CreateChangaRequest("Servicio de Plomeria", "Servicio de Plomeria", basePhotoUrl + "Servicio+de+Plomeria", Set.of("Plomeria", "Hogar", "Mantenimiento"), serviceAreaRequest));
        ChangaOverviewDTO changa2 = changaService.createChanga(new CreateChangaRequest("Servicio de Poda", "Servicio de Poda", basePhotoUrl + "Servicio+de+Poda", Set.of("Poda", "Hogar", "Exterior"), serviceAreaRequest));

        authService.login(new LoginRequest("sofia@email.com", "password"));
        transactionService.requestChanga(new HireChangaRequest(changa1.getId(), new WorkAreaDetailsDTO(null, basePhotoUrl + "Area+Trabajo", "Descripcion trabajo")));

        authService.login(new LoginRequest("matias@email.com", "password"));
        inquiryService.createQuestion(changa2.getId(), "Buenas, hacen extracciones de raiz?");

    }
}
